WORKDIR = "${TMPDIR}/work/${MULTIMACH_TARGET_SYS}/${PN}/${EXTENDPE}${PV}-${PR}"

do_rm_old_works() {
    subdir="${TMPDIR}/work/${MULTIMACH_TARGET_SYS}/${PN}"
    if [ -d "$subdir" ]; then
       for v in `ls $subdir`; do
           if [ "$subdir/$v" != "${WORKDIR}" ]; then
              echo "Deleting old work dir $v"
              rm -rf $subdir/$v
           fi
       done
    fi
}

addtask rm_old_works before do_configure
