#
# Removes source after build
#
# To use it add that line to conf/local.conf:
#
# INHERIT += "rm_work"
#

do_rm_work () {
    cd ${WORKDIR}
    for dir in *
    do
        if [ `basename ${S}` == $dir ]; then
            rm -rf $dir/*
        elif [ $dir != 'temp' ]; then
            rm -rf $dir
        fi
    done
}

addtask rm_work before do_build
addtask rm_work after do_package
