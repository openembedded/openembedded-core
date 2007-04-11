DESCRIPTION = "Util-linux is a suite of essential utilities for any Linux system."
SECTION = "base"
LICENSE = "GPL"
DEPENDS = "zlib ncurses"

inherit autotools

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/utils/util-linux/util-linux-${PV}.tar.bz2 \
           file://gcc34.patch;patch=1 \
           file://MCONFIG \
           file://make_include \
           file://swapargs.h \
	   file://fdiskbsdlabel_thumb.diff;patch=1 \
	   file://umount.diff;patch=1 \
           file://defines.h"

PACKAGES_prepend = "util-linux-fdisk util-linux-cfdisk util-linux-sfdisk util-linux-swaponoff util-linux-losetup util-linux-umount util-linux-mount util-linux-readprofile "

FILES_${PN}-doc += "/usr/share/misc/getopt/getopt-*.*"

FILES_util-linux-fdisk = "/sbin/fdisk.${PN}"
FILES_util-linux-cfdisk = "/sbin/cfdisk"
FILES_util-linux-sfdisk = "/sbin/sfdisk"
FILES_util-linux-swaponoff = "/sbin/swapon.${PN} /sbin/swapoff.${PN}"
FILES_util-linux-losetup = "/sbin/losetup.${PN}"
FILES_util-linux-mount = "/bin/mount.${PN}"
FILES_util-linux-umount = "/bin/umount.${PN}"
FILES_util-linux-readprofile = "/usr/sbin/readprofile"

RRECOMMENDS_${PN} = "util-linux-fdisk util-linux-cfdisk util-linux-sfdisk util-linux-mount util-linux-readprofile "
RDEPENDS_${PN} = "util-linux-umount util-linux-swaponoff util-linux-losetup"

do_compile () {
	set -e
	install ${WORKDIR}/MCONFIG ${S}/MCONFIG
	install ${WORKDIR}/make_include ${S}/make_include
	install ${WORKDIR}/swapargs.h ${S}/mount/swapargs.h
	install ${WORKDIR}/defines.h ${S}/defines.h
	oe_runmake ARCH=${TARGET_ARCH} CPU= CPUOPT= \
		'OPT=${CFLAGS}' 'CC=${CC}' 'LD=${LD}' \
		'LDFLAGS=${LDFLAGS}'
}

checklink() {
	local targ link value
	targ="$1"
	link="$2"
	ext="$3"
	if test -h "$link"
	then
		value="$(readlink "$link")"
		if test "$value" = "$targ"
		then
			rm "$link"
			ln -s "$targ"."${PN}" "$link$ext"
			return 0
		else
			echo "$link: '$value' found '$targ' expected" >&2
			return 1
		fi
	else
		echo "$link: not a symbolic link" >&2
		return 1
	fi
}

do_install () {
	# with ccache the timestamps on compiled files may
	# end up earlier than on their inputs, this allows
	# for the resultant compilation in the install step.
	oe_runmake ARCH=${TARGET_ARCH} CPU= CPUOPT= \
		'OPT=${CFLAGS}' 'CC=${CC}' 'LD=${LD}' \
		'LDFLAGS=${LDFLAGS}' 'DESTDIR=${D}' install

	test -f ${D}${base_sbindir}/pivot_root && \
	mv ${D}${base_sbindir}/pivot_root ${D}${base_sbindir}/pivot_root.${PN}
	mv ${D}${base_bindir}/dmesg ${D}${base_bindir}/dmesg.${PN}
	mv ${D}${base_bindir}/kill ${D}${base_bindir}/kill.${PN}
	mv ${D}${base_bindir}/more ${D}${base_bindir}/more.${PN}
	mv ${D}${base_sbindir}/hwclock ${D}${base_sbindir}/hwclock.${PN}
	mv ${D}${base_sbindir}/mkswap ${D}${base_sbindir}/mkswap.${PN}
	mv ${D}${base_sbindir}/shutdown ${D}${base_sbindir}/shutdown.${PN}
	mv ${D}${base_sbindir}/sln ${D}${base_sbindir}/sln.${PN}
	mv ${D}${base_sbindir}/mkfs.minix ${D}${base_sbindir}/mkfs.minix.${PN}
	mv ${D}${base_sbindir}/fsck.minix ${D}${base_sbindir}/fsck.minix.${PN}
	mv ${D}${bindir}/hexdump ${D}${bindir}/hexdump.${PN}
	mv ${D}${bindir}/last ${D}${bindir}/last.${PN}
	mv ${D}${bindir}/logger ${D}${bindir}/logger.${PN}
	mv ${D}${bindir}/mesg ${D}${bindir}/mesg.${PN}
	mv ${D}${bindir}/renice ${D}${bindir}/renice.${PN}
	mv ${D}${bindir}/wall ${D}${bindir}/wall.${PN}

	mv ${D}${base_sbindir}/losetup ${D}${base_sbindir}/losetup.${PN}
	mv ${D}${base_sbindir}/swapon ${D}${base_sbindir}/swapon.${PN}
	mv ${D}${base_bindir}/umount ${D}${base_bindir}/umount.${PN}
	mv ${D}${base_bindir}/mount ${D}${base_bindir}/mount.${PN}
	mv ${D}${base_sbindir}/fdisk ${D}${base_sbindir}/fdisk.${PN}

	checklink swapon ${D}${base_sbindir}/swapoff ".${PN}"
	checklink shutdown ${D}${base_sbindir}/reboot ".${PN}"
	checklink shutdown ${D}${base_sbindir}/halt ".${PN}"
	checklink shutdown ${D}${base_sbindir}/fastboot ""
	checklink shutdown ${D}${base_sbindir}/fasthalt ""
}

pkg_postinst_${PN} () {
	update-alternatives --install ${base_bindir}/dmesg dmesg dmesg.${PN} 100
	update-alternatives --install ${base_bindir}/kill kill kill.${PN} 100
	update-alternatives --install ${base_bindir}/more more more.${PN} 100
	update-alternatives --install ${base_sbindir}/mkswap mkswap mkswap.${PN} 100
	test -x ${base_sbindir}/pivot_root.${PN} && \
	update-alternatives --install ${base_sbindir}/pivot_root pivot_root pivot_root.${PN} 100
	update-alternatives --install ${base_sbindir}/sln sln sln.${PN} 100
	update-alternatives --install ${base_sbindir}/mkfs.minix mkfs.minix mkfs.minix.${PN} 100
	update-alternatives --install ${base_sbindir}/fsck.minix fsck.minix fsck.minix.${PN} 100
	update-alternatives --install ${bindir}/hexdump hexdump hexdump.${PN} 100
	update-alternatives --install ${bindir}/last last last.${PN} 100
	update-alternatives --install ${bindir}/logger logger logger.${PN} 100
	update-alternatives --install ${bindir}/mesg mesg mesg.${PN} 100
	update-alternatives --install ${bindir}/renice renice renice.${PN} 100
	update-alternatives --install ${bindir}/wall wall wall.${PN} 100
	
	# There seems to be problem, atleast on nslu2, with these, untill they are
	# fixed the busybox ones have higher priority
	update-alternatives --install ${base_sbindir}/hwclock hwclock hwclock.${PN} 10
	update-alternatives --install ${base_sbindir}/shutdown shutdown shutdown.${PN} 10
	update-alternatives --install ${base_sbindir}/reboot reboot reboot.${PN} 10
	update-alternatives --install ${base_sbindir}/halt halt halt.${PN} 10
}

pkg_prerm_${PN} () {
	test -x ${base_sbindir}/pivot_root.${PN} && \
	update-alternatives --remove pivot_root pivot_root.${PN}
	update-alternatives --remove dmesg dmesg.${PN}
	update-alternatives --remove kill kill.${PN}
	update-alternatives --remove more more.${PN}
	update-alternatives --remove halt halt.${PN}
	update-alternatives --remove hwclock hwclock.${PN}
	update-alternatives --remove mkswap mkswap.${PN}
	update-alternatives --remove reboot reboot.${PN}
	update-alternatives --remove shutdown shutdown.${PN}
	update-alternatives --remove sln sln.${PN}
	update-alternatives --remove mkfs.minix mkfs.minix.${PN}
	update-alternatives --remove fsck.minix fsck.minix.${PN}
	update-alternatives --remove hexdump hexdump.${PN}
	update-alternatives --remove last last.${PN}
	update-alternatives --remove logger logger.${PN}
	update-alternatives --remove mesg mesg.${PN}
	update-alternatives --remove renice renice.${PN}
	update-alternatives --remove wall wall.${PN}
}

pkg_postinst_util-linux-fdisk () {
	update-alternatives --install ${base_sbindir}/fdisk fdisk fdisk.${PN} 100
}

pkg_prerm_util-linux-fdisk () {
	update-alternatives --remove fdisk fdisk.${PN}
}

pkg_postinst_util-linux-mount () {
	update-alternatives --install ${base_bindir}/mount mount mount.${PN} 100
}

pkg_prerm_util-linux-mount () {
	update-alternatives --remove mount mount.${PN}
}

pkg_postinst_util-linux-umount () {
	update-alternatives --install ${base_bindir}/umount umount umount.${PN} 100
}

pkg_prerm_util-linux-umount () {
	update-alternatives --remove umount umount.${PN}
}

pkg_postinst_util-linux-losetup () {
	update-alternatives --install ${base_sbindir}/losetup losetup losetup.${PN} 100
}

pkg_prerm_util-linux-losetup () {
	update-alternatives --remove losetup losetup.${PN}
}

pkg_postinst_util-linux-swaponoff () {
	update-alternatives --install ${base_sbindir}/swapoff swapoff swapoff.${PN} 100
	update-alternatives --install ${base_sbindir}/swapon swapon swapon.${PN} 100
}

pkg_prerm_util-linux-swaponoff () {
	update-alternatives --remove swapoff swapoff.${PN}
	update-alternatives --remove swapon swapon.${PN}
}
