#!/bin/sh

my_dir=$(dirname $0)
my_name=$(basename $0)

help_sub()
{
    echo "Use: "$my_name": -f file_edmexport_war [ -s show web.xml inside the war file ] [ -z use zip instead of jar ]" >&2
    exit 1
}

flush_stdin()
{
    stty min 0 time 1 -icanon
    while [ -n "$(dd bs=1 count=1 2>/dev/null)" ]
    do
        continue
    done
    stty min 1 time 0 icanon
}

test $# -eq 0 && help_sub

EDMExportWar=""
show_web_xml=0
use_zip=0

while [ -n "$*" ];
do
    case $1 in
        "-f" )
            shift
            EDMExportWar="$1"
            ;;
        "-s" )
            show_web_xml=1
            ;;
        "-z" )
            use_zip=1
            ;;
        "-h" | "?" )
            help_sub
            ;;
        * )
            help_sub
            ;;
    esac
    shift
done



[[ -z "$EDMExportWar" || ! -f "$EDMExportWar" ]] && echo "File $EDMExportWar does no exist" && exit 1

jar_cmd=$( which jar 2>/dev/null)
[[ -z "$jar_cmd" || ! -x "$jar_cmd" ]] && echo "Command jar does no exist" && exit 1

zip_cmd=$( which zip 2>/dev/null)
[[ -z "$zip_cmd" || ! -x "$zip_cmd" ]] && echo "Command zip does no exist" && exit 1

unzip_cmd=$( which unzip 2>/dev/null)
[[ -z "$unzip_cmd" || ! -x "$unzip_cmd" ]] && echo "Command unzip does no exist" && exit 1



if [ $show_web_xml -eq 0 ]; then
    dspace_cfg=""
    until [ -n "$dspace_cfg" ]; do
        echo "Enter the path of the dspace config file"
        flush_stdin
        read dspace_cfg
    done
    
    if [ ! -f "$dspace_cfg" ]; then
        response=""
        until [ "$response" = "y" -o "$response" = "n" ]; do
            echo "File $dspace_cfg does not exist in this machine. Continue? (y/n)"
            flush_stdin
            read response
        done
        test "$response" = "n" && exit 0
    fi

    if [ $use_zip -eq 1 ]; then
        $unzip_cmd -l "$EDMExportWar" | grep -q 'web.xml' && $unzip_cmd -t "$EDMExportWar" | grep 'web.xml' | grep -iq 'ok'\
         && $unzip_cmd -p "$EDMExportWar" "WEB-INF/web.xml"\
        | sed -r -e '\#<param-name>dspace-config</param-name>#{
        N
        s#(<param-name>dspace-config</param-name>.+<param-value>)[^<]+(</param-value>)#\1'"$dspace_cfg"'\2#
        }' | $zip_cmd "$EDMExportWar" "WEB-INF/web.xml" - && echo "File $EDMExportWar changed successfully"
    else
        mkdir -p /tmp/edmexport/WEB-INF/
        $unzip_cmd -l "$EDMExportWar" | grep -q 'web.xml' && $unzip_cmd -t "$EDMExportWar" | grep 'web.xml' | grep -iq 'ok'\
         && $unzip_cmd -p "$EDMExportWar" "WEB-INF/web.xml"\
        | sed -r -e '\#<param-name>dspace-config</param-name>#{
        N
        s#(<param-name>dspace-config</param-name>.+<param-value>)[^<]+(</param-value>)#\1'"$dspace_cfg"'\2#
        }' > /tmp/edmexport/WEB-INF/web.xml && $jar_cmd uf "$EDMExportWar" -C /tmp/edmexport "WEB-INF/web.xml"\
        && echo "File $EDMExportWar changed successfully" && rm -rf /tmp/edmexport
        test -d /tmp/edmexport && rm -rf /tmp/edmexport
    fi
else
    $unzip_cmd -l "$EDMExportWar" | grep -q 'web.xml' && $unzip_cmd -t "$EDMExportWar" | grep 'web.xml' | grep -iq 'ok'\
        && $unzip_cmd -p "$EDMExportWar" "WEB-INF/web.xml" | less
fi
