SECTION = "base"
SUMMARY = "Kernel module utilities"
DESCRIPTION = "These utilities are intended to make a Linux modular kernel \
manageable for all users, administrators and distribution maintainers."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b"
DEPENDS = "bison-native"
PR = "r9"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/kernel/modutils/v2.4/modutils-${PV}.tar.bz2 \
           file://lex.l.diff;patch=1 \
           file://modutils-notest.patch;patch=1 \
           file://configure.patch;patch=1 \
           file://program_prefix.patch;patch=1 \
           file://armeb.patch;patch=1 \
	   file://gcc4.patch;patch=1"

SRC_URI[md5sum] = "bac989c74ed10f3bf86177fc5b4b89b6"
SRC_URI[sha256sum] = "ab4c9191645f9ffb455ae7c014d8c45339c13a1d0f6914817cfbf30a0bc56bf0"

inherit autotools

# modutils go in /sbin
sbindir = "/sbin"
EXTRA_OECONF = "--disable-strip"
export BUILDCC = "${BUILD_CC}"
export BUILDCFLAGS = "${BUILD_CFLAGS}"

do_install () {
	oe_runmake 'DESTDIR=${D}' install
	install -d ${D}${sysconfdir}
	rm ${D}${base_sbindir}/lsmod
	install -d ${D}${base_bindir}/
	ln -s ../sbin/insmod ${D}${base_bindir}/lsmod
        for f in bin/lsmod sbin/insmod sbin/rmmod sbin/modprobe sbin/modinfo sbin/depmod; do                mv ${D}/$f ${D}/$f.24
        done
}

pkg_postinst_modutils () {
#!/bin/sh
for f in sbin/insmod sbin/modprobe sbin/rmmod bin/lsmod; do
	bn=`basename $f`
	update-alternatives --install /$f $bn /$f.24 10
done
if test -n "$D"; then
	D="-r $D"
	if test -n "`which ${TARGET_PREFIX}depmod-2.4`"; then
		for kerneldir in `ls -p $D/lib/modules|grep /`; do
			kernelver=`basename $kerneldir`
			${TARGET_PREFIX}depmod-2.4 -a -b $D -C ${IMAGE_ROOTFS}/${sysconfdir}/modules.conf -r $kernelver
		done
	fi
fi
update-rc.d $D modutils.sh start 20 S .
}

pkg_prerm_modutils () {
#!/bin/sh
for f in sbin/insmod sbin/modprobe sbin/rmmod sbin/depmod sbin/modinfo bin/lsmod; do
bn=`basename $f`
	update-alternatives --remove $bn /$f.24
done
if test -n "$D"; then
	D="-r $D"
fi
update-rc.d $D modutils.sh remove
}

pkg_postinst_modutils-depmod() {
#!/bin/sh
update-alternatives --install /sbin/depmod depmod /sbin/depmod.24 10
}

pkg_postinst_modutils-modinfo() {
#!/bin/sh
update-alternatives --install /sbin/modinfo modinfo /sbin/modinfo.24 10
}

pkg_prerm_modutils-depmod() {
#!/bin/sh
update-alternatives --remove depmod /sbin/depmod.24
}

pkg_prerm_modutils-modinfo() {
#!/bin/sh
update-alternatives --remove modinfo /sbin/modinfo.24
}

PACKAGES = "modutils-depmod modutils-modinfo modutils-doc modutils"

FILES_modutils-depmod = "sbin/depmod.24"
FILES_modutils-modinfo = "sbin/modinfo.24"
RDEPENDS_modutils = "modutils-depmod"
