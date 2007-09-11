require binutils_${PV}.bb
require binutils-cross.inc
PR = "r1"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/binutils-${PV}"

do_stage () {
	oe_runmake install

	MULTILIBDIR=`gcc -print-multi-os-directory`

	# We don't really need these, so we'll remove them...
	rm -rf ${CROSS_DIR}/lib/ldscripts
	rm -rf ${CROSS_DIR}/share/info
	rm -rf ${CROSS_DIR}/share/locale
	rm -rf ${CROSS_DIR}/share/man
	rmdir ${CROSS_DIR}/share || :
	rmdir ${CROSS_DIR}/${libdir}/gcc-lib || :
	rmdir ${CROSS_DIR}/${libdir} || :
	rmdir ${CROSS_DIR}/${prefix} || :

	# We want to move this into the target specific location
	mkdir -p ${CROSS_DIR}/${TARGET_SYS}/lib
	mv -f ${CROSS_DIR}/lib/${MULTILIBDIR}/libiberty.a ${CROSS_DIR}/${TARGET_SYS}/lib
	rmdir ${CROSS_DIR}/lib/${MULTILIBDIR} || :
}
