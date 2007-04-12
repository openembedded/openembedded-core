do_unpack2() {
    cd ${WORKDIR} && tar jxf ${PN}-${PV}.tar.bz2?format=raw
}

addtask unpack2 after do_unpack before do_patch
