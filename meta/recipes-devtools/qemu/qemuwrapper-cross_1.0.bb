DESCRIPTION = "Qemu wrapper script"
LICENSE = "MIT"
PR = "r0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit qemu

do_install () {
	install -d ${D}${bindir_crossscripts}/

	echo "#!/bin/sh" > ${D}${bindir_crossscripts}/qemuwrapper
	echo exec env ${@qemu_target_binary(d)} \"\$@\" >> ${D}${bindir_crossscripts}/qemuwrapper
	chmod +x ${D}${bindir_crossscripts}/qemuwrapper
}

SYSROOT_PREPROCESS_FUNCS += "qemuwrapper_sysroot_preprocess"

qemuwrapper_sysroot_preprocess () {
	sysroot_stage_dir ${D}${bindir_crossscripts} ${SYSROOT_DESTDIR}${bindir_crossscripts}
}
