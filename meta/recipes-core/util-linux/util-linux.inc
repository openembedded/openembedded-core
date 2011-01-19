SUMMARY = "A suite of basic system administration utilities."
DESCRIPTION = "Util-linux includes a suite of basic system administration utilities \
commonly found on most Linux systems.  Some of the more important utilities include \
disk partitioning, kernel message management, filesystem creation, and system login."

SECTION = "base"

# note that `lscpu' is under GPLv3+
LICENSE = "GPLv2+ & LGPLv2.1+ & BSD"
LICENSE_util-linux-lscpu = "GPLv3+"

LIC_FILES_CHKSUM = "file://README.licensing;md5=1530e36fe1304d4535513de90a290df9 \
                    file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://licenses/COPYING.GPL;md5=8ca43cbc842c2336e835926c2166c28b \
                    file://licenses/COPYING.UCB;md5=263860f8968d8bafa5392cab74285262 \
                    file://getopt/COPYING;md5=8ca43cbc842c2336e835926c2166c28b"

DEPENDS = "zlib ncurses virtual/libintl gettext"
DEPENDS_virtclass-native = "zlib-native ncurses-native lzo-native gettext-native"

inherit autotools

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/utils/util-linux-ng/v${MAJOR_VERSION}/util-linux-ng-${PV}.tar.bz2 \
           file://MCONFIG \
           file://swapargs.h \
           file://defines.h \
           file://make_include \
           "

PACKAGES_prepend = "util-linux-fdisk util-linux-cfdisk util-linux-sfdisk util-linux-swaponoff \
                    util-linux-losetup util-linux-umount util-linux-mount util-linux-readprofile \
                    util-linux-libblkid util-linux-libblkid-dev util-linux-libuuid util-linux-libuuid-dev "

PACKAGES_virtclass-native = ""

S = "${WORKDIR}/util-linux-ng-${PV}"

EXTRA_OECONF = "--disable-use-tty-group --disable-makeinstall-chown --enable-elvtune --enable-init --enable-kill --enable-last \
 --enable-mesg --enable-partx --enable-raw --enable-rdev --enable-reset \
  --disable-login-utils --enable-write --enable-arch --enable-mount --with-fsprobe=builtin --enable-libuuid --enable-libblkid --enable-fsck"

FILES_${PN}-doc += "/usr/share/getopt/getopt-*.*"

FILES_util-linux-fdisk = "/sbin/fdisk.${PN}"
FILES_util-linux-cfdisk = "/sbin/cfdisk"
FILES_util-linux-sfdisk = "/usr/sbin/sfdisk"
FILES_util-linux-swaponoff = "/sbin/swapon.${PN} /usr/sbin/swapoff.${PN}"
FILES_util-linux-losetup = "/sbin/losetup.${PN}"
FILES_util-linux-mount = "/bin/mount.${PN}"
FILES_util-linux-umount = "/bin/umount.${PN}"
FILES_util-linux-readprofile = "/sbin/readprofile"

FILES_util-linux-libblkid = "/usr/lib/libblkid.so.*"
FILES_util-linux-libblkid-dev = "/usr/lib/libblkid.so /usr/lib/libblkid.a /usr/lib/libblkid.la /usr/include/blkid"
FILES_util-linux-libuuid = "/usr/lib/libuuid.so.*"
FILES_util-linux-libuuid-dev = "/usr/lib/libuuid.so /usr/lib/libuuid.a /usr/lib/libuuid.la /usr/include/uuid"

RRECOMMENDS_${PN} = "util-linux-fdisk util-linux-cfdisk util-linux-sfdisk util-linux-mount util-linux-readprofile "
RDEPENDS_${PN} = "util-linux-umount util-linux-swaponoff util-linux-losetup"

RRECOMMENDS_${PN}_virtclass-native = ""
RDEPENDS_${PN}_virtclass-native = ""

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

	mkdir -p ${D}${base_bindir}

        sbinprogs="agetty blockdev ctrlaltdel cfdisk vipw vigr"
        sbinprogs_a="pivot_root hwclock mkswap shutdown mkfs.minix fsck.minix losetup swapon fdisk readprofile fsck blkid sln"
        usrbinprogs_a="chfn chsh hexdump last logger mesg newgrp renice wall setsid chrt"
        binprogs_a="dmesg kill more umount mount login reset"

        if [ "${base_sbindir}" != "${sbindir}" ]; then
        	mkdir -p ${D}${base_sbindir}
                for p in $sbinprogs $sbinprogs_a; do
                        if [ -f "${D}${sbindir}/$p" ]; then
                                mv "${D}${sbindir}/$p" "${D}${base_sbindir}/$p"
                        fi
                done
        fi

        if [ "${base_bindir}" != "${bindir}" ]; then
        	mkdir -p ${D}${base_bindir}
                for p in $binprogs_a; do
                        if [ -f "${D}${bindir}/$p" ]; then
                                mv "${D}${bindir}/$p" "${D}${base_bindir}/$p"
                        fi
                done
        fi

        for p in $usrbinprogs_a; do
                if [ -f "${D}${bindir}/$p" ]; then
                        mv "${D}${bindir}/$p" "${D}${bindir}/$p.${PN}"
                fi
        done

        for p in $binprogs_a; do
                if [ -f "${D}${base_bindir}/$p" ]; then
                        mv "${D}${base_bindir}/$p" "${D}${base_bindir}/$p.${PN}"
                fi
        done

        for p in $sbinprogs_a; do
                if [ -f "${D}${base_sbindir}/$p" ]; then
                        mv "${D}${base_sbindir}/$p" "${D}${base_sbindir}/$p.${PN}"
                fi
        done
	
	install -d ${D}${sysconfdir}/default/
	echo 'MOUNTALL="-t nonfs,nosmbfs,noncpfs"' > ${D}${sysconfdir}/default/mountall

	ln -sf ${sbindir}/swapon ${D}${sbindir}/swapoff.${PN}
	ln -sf ${sbindir}/shutdown ${D}${sbindir}/reboot.${PN}
	ln -sf ${sbindir}/shutdown ${D}${sbindir}/halt.${PN}
	ln -sf ${sbindir}/shutdown ${D}${sbindir}/fastboot
	ln -sf ${sbindir}/shutdown ${D}${sbindir}/fasthalt
}

pkg_postinst_${PN} () {
	update-alternatives --install ${base_bindir}/dmesg dmesg dmesg.${PN} 100
	update-alternatives --install ${base_bindir}/kill kill kill.${PN} 100
	update-alternatives --install ${base_bindir}/more more more.${PN} 100
	update-alternatives --install ${base_sbindir}/mkswap mkswap mkswap.${PN} 100
	test -x ${base_sbindir}/pivot_root.${PN} && \
	update-alternatives --install ${base_sbindir}/pivot_root pivot_root pivot_root.${PN} 100
#	update-alternatives --install ${base_sbindir}/sln sln sln.${PN} 100
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
#	update-alternatives --remove sln sln.${PN}
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

BBCLASSEXTEND = "native"
