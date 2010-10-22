#
# Removes source after build
#
# To use it add that line to conf/local.conf:
#
# INHERIT += "rm_work"
#

RMWORK_ORIG_TASK := "${BB_DEFAULT_TASK}"
BB_DEFAULT_TASK = "rm_work_all"

do_rm_work () {
    # Ensure pseudo is no longer active
    if [ -d ${WORKDIR}/pseudo ]; then
        ${FAKEROOT} -S
    fi
    cd ${WORKDIR}
    for dir in *
    do
        if [ `basename ${S}` = $dir ]; then
            rm -rf $dir
        elif [ $dir != 'temp' ]; then
            rm -rf $dir
        fi
    done
    # Need to add pseudo back or subsqeuent work in this workdir
    # might fail since setscene may not rerun to recreate it
    mkdir ${WORKDIR}/pseudo/
}
addtask rm_work after do_${RMWORK_ORIG_TASK}

do_rm_work_all () {
	:
}
do_rm_work_all[recrdeptask] = "do_rm_work"
addtask rm_work_all after do_rm_work
