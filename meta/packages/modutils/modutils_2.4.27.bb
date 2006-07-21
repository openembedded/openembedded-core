SECTION = "base"
DESCRIPTION = "These utilities are intended to make a Linux modular kernel \
manageable for all users, administrators and distribution maintainers."
LICENSE = "GPLv2"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/files"
PR = "r7"

SRC_URI = "ftp://ftp.kernel.org/pub/linux/utils/kernel/modutils/v2.4/modutils-${PV}.tar.bz2 \
           file://lex.l.diff;patch=1 \
           file://modutils-notest.patch;patch=1 \
           file://configure.patch;patch=1 \
           file://program_prefix.patch;patch=1 \
           file://armeb.patch;patch=1 \
	   file://gcc4.patch;patch=1"

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
		for kerneldir in `ls -p ${IMAGE_ROOTFS}/lib/modules|grep /`; do
			kernelver=`basename $kerneldir`
			${TARGET_PREFIX}depmod-2.4 -a -b ${IMAGE_ROOTFS} -C ${IMAGE_ROOTFS}/${sysconfdir}/modules.conf -r $kernelver
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
