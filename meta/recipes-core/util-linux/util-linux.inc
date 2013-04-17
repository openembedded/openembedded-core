SUMMARY = "A suite of basic system administration utilities."
DESCRIPTION = "Util-linux includes a suite of basic system administration utilities \
commonly found on most Linux systems.  Some of the more important utilities include \
disk partitioning, kernel message management, filesystem creation, and system login."

SECTION = "base"

LICENSE = "GPLv2+ & LGPLv2.1+ & BSD"

LIC_FILES_CHKSUM = "file://README.licensing;md5=679af3e410ee112a5ec94de6501bf5c3 \
                    file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://Documentation/licenses/COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://Documentation/licenses/COPYING.LGPLv2.1;md5=4fbd65380cdd255951079008b364516c \
                    file://Documentation/licenses/COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af \
                    file://Documentation/licenses/COPYING.UCB;md5=263860f8968d8bafa5392cab74285262 \
                    file://libuuid/COPYING;md5=aa3ac5b4a5bcb707f714f78794011b80 \
                    file://libmount/COPYING;md5=152f98bc301a5ea649769ede7203ac82 \
                    file://libblkid/COPYING;md5=152f98bc301a5ea649769ede7203ac82"

inherit autotools gettext pkgconfig systemd
DEPENDS = "zlib ncurses"
DEPENDS_append_class-native = " lzo-native"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/util-linux/v${MAJOR_VERSION}/util-linux-${PV}.tar.bz2 \
           file://MCONFIG \
           file://defines.h \
           file://make_include \
           file://swapargs.h \
           "

PACKAGES =+ "util-linux-agetty util-linux-fdisk util-linux-cfdisk util-linux-sfdisk \
             util-linux-swaponoff util-linux-losetup util-linux-umount \
             util-linux-mount util-linux-readprofile util-linux-libblkid \
             util-linux-libmount util-linux-libmount-dev \
             util-linux-libblkid-dev util-linux-libuuid util-linux-libuuid-dev \
             util-linux-uuidgen util-linux-lscpu util-linux-fsck util-linux-blkid \
             util-linux-mkfs util-linux-mcookie util-linux-reset util-linux-uuidd"

EXTRA_OECONF = "--libdir=${base_libdir} --disable-use-tty-group \
                --disable-makeinstall-chown --enable-elvtune --enable-init \
                --enable-kill --enable-last --enable-mesg --enable-partx \
                --enable-raw --enable-rdev --enable-reset --disable-login \
                --disable-vipw --disable-newgrp --disable-chfn-chsh --disable-su \
                --enable-write --enable-arch --enable-mount --with-fsprobe=builtin \
                --enable-libuuid --enable-libblkid --enable-fsck --without-udev \
		usrsbin_execdir='${sbindir}' \
"

# Respect the systemd feature for uuidd
EXTRA_OECONF += "${@base_contains('DISTRO_FEATURES', 'systemd', '--enable-socket-activation', '--disable-socket-activation', d)}"
EXTRA_OECONF += "${@base_contains('DISTRO_FEATURES', 'systemd', '--with-systemdsystemunitdir=${systemd_unitdir}/system/', '--without-systemdsystemunitdir', d)}"

EXTRA_OECONF_append_class-native = " --disable-login --disable-su"

FILES_${PN}-doc += "${datadir}/getopt/getopt-*.*"

FILES_util-linux-agetty = "${base_sbindir}/agetty"
FILES_util-linux-fdisk = "${base_sbindir}/fdisk.${BPN}"
FILES_util-linux-cfdisk = "${base_sbindir}/cfdisk"
FILES_util-linux-sfdisk = "${sbindir}/sfdisk"
FILES_util-linux-swaponoff = "${base_sbindir}/swapon.${BPN} ${base_sbindir}/swapoff.${BPN}"
FILES_util-linux-losetup = "${base_sbindir}/losetup.${BPN}"
FILES_util-linux-mount = "${base_bindir}/mount.${BPN}"
FILES_util-linux-mcookie = "${bindir}/mcookie"
FILES_util-linux-umount = "${base_bindir}/umount.${BPN}"
FILES_util-linux-readprofile = "${base_sbindir}/readprofile.${BPN}"
FILES_util-linux-uuidgen = "${bindir}/uuidgen"
FILES_util-linux-uuidd = "${sbindir}/uuidd"
FILES_util-linux-reset = "${base_bindir}/reset"

FILES_util-linux-libblkid = "${base_libdir}/libblkid.so.*"
FILES_util-linux-libblkid-dev = "${base_libdir}/libblkid.so ${base_libdir}/libblkid.la ${includedir}/blkid ${libdir}/pkgconfig/blkid.pc"
FILES_util-linux-libmount = "${base_libdir}/libmount.so.*"
FILES_util-linux-libmount-dev = "${base_libdir}/libmount.so ${base_libdir}/libmount.la ${includedir}/libmount ${libdir}/pkgconfig/mount.pc"
FILES_util-linux-libuuid = "${base_libdir}/libuuid.so.*"
FILES_util-linux-libuuid-dev = "${base_libdir}/libuuid.so ${base_libdir}/libuuid.la ${includedir}/uuid ${libdir}/pkgconfig/uuid.pc"
FILES_util-linux-lscpu = "${bindir}/lscpu"

FILES_util-linux-fsck = "${base_sbindir}/fsck*"
FILES_util-linux-mkfs = "${sbindir}/mkfs"

# Util-linux' blkid replaces the e2fsprogs one
FILES_util-linux-blkid = "${base_sbindir}/blkid*"
RCONFLICTS_util-linux-blkid = "e2fsprogs-blkid"
RREPLACES_util-linux-blkid = "e2fsprogs-blkid"

# reset calls 'tput'
RDEPENDS_util-linux-reset += "ncurses"

RDEPENDS_${PN} = "util-linux-umount util-linux-swaponoff util-linux-losetup"
RRECOMMENDS_${PN} = "util-linux-fdisk util-linux-cfdisk util-linux-sfdisk util-linux-mount util-linux-readprofile util-linux-mkfs "

RRECOMMENDS_${PN}_class-native = ""
RDEPENDS_${PN}_class-native = ""

SYSTEMD_PACKAGES = "${PN}-uuidd"
SYSTEMD_SERVICE_${PN}-uuidd = "uuidd.service"

do_compile () {
	set -e
	install ${WORKDIR}/MCONFIG ${S}/MCONFIG
	install ${WORKDIR}/make_include ${S}/make_include
	install ${WORKDIR}/swapargs.h ${S}/mount-deprecated/swapargs.h
	install ${WORKDIR}/defines.h ${S}/defines.h
	oe_runmake ARCH=${TARGET_ARCH} CPU= CPUOPT= 'OPT=${CFLAGS}' 
}

do_install () {
	# with ccache the timestamps on compiled files may
	# end up earlier than on their inputs, this allows
	# for the resultant compilation in the install step.
	oe_runmake ARCH=${TARGET_ARCH} CPU= CPUOPT= \
		'OPT=${CFLAGS}' 'CC=${CC}' 'LD=${LD}' \
		'LDFLAGS=${LDFLAGS}' 'DESTDIR=${D}' install

	mkdir -p ${D}${base_bindir}

        sbinprogs="agetty ctrlaltdel cfdisk vipw vigr"
        sbinprogs_a="pivot_root hwclock mkswap mkfs.minix fsck.minix losetup swapon fdisk readprofile fsck blkid blockdev"
        usrbinprogs_a="chfn chsh hexdump last logger mesg newgrp renice utmpdump wall setsid chrt flock getopt"
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

	install -d ${D}${sysconfdir}/default/
	echo 'MOUNTALL="-t nonfs,nosmbfs,noncpfs"' > ${D}${sysconfdir}/default/mountall

	ln -sf swapon.${BPN}   ${D}${base_sbindir}/swapoff.${BPN}
	rm -f ${D}${sbindir}/swapoff

	rm -f ${D}${bindir}/chkdupexe
}

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"

ALTERNATIVE_${PN}  = "dmesg kill more mkswap blockdev pivot_root"
ALTERNATIVE_${PN} += "mkfs.minix hexdump last logger mesg renice wall"
ALTERNATIVE_${PN} += "setsid chrt flock hwclock utmpdump eject getopt"

ALTERNATIVE_LINK_NAME[dmesg] = "${base_bindir}/dmesg"
ALTERNATIVE_LINK_NAME[kill] = "${base_bindir}/kill"
ALTERNATIVE_LINK_NAME[more] = "${base_bindir}/more"
ALTERNATIVE_LINK_NAME[mkswap] = "${base_sbindir}/mkswap"
ALTERNATIVE_LINK_NAME[blockdev] = "${base_sbindir}/blockdev"
ALTERNATIVE_LINK_NAME[pivot_root] = "${base_sbindir}/pivot_root"
ALTERNATIVE_LINK_NAME[mkfs.minix] = "${base_sbindir}/mkfs.minix"
ALTERNATIVE_LINK_NAME[eject] = "${bindir}/eject"
ALTERNATIVE_LINK_NAME[getopt] = "${base_bindir}/getopt"
ALTERNATIVE_TARGET[getopt] = "${bindir}/getopt"

ALTERNATIVE_${PN}-doc = "mountpoint.1 last.1 mesg.1 wall.1 sulogin.8 utmpdump.1"

ALTERNATIVE_LINK_NAME[last.1] = "${mandir}/man1/last.1"
ALTERNATIVE_LINK_NAME[mesg.1] = "${mandir}/man1/mesg.1"
ALTERNATIVE_LINK_NAME[mountpoint.1] = "${mandir}/man1/mountpoint.1"
ALTERNATIVE_LINK_NAME[sulogin.8] = "${mandir}/man8/sulogin.8"
ALTERNATIVE_LINK_NAME[utmpdump.1] = "${mandir}/man1/utmpdump.1"
ALTERNATIVE_LINK_NAME[wall.1] = "${mandir}/man1/wall.1"

# There seems to be problem, atleast on nslu2, with these, untill they are
# fixed the busybox ones have higher priority
ALTERNATIVE_PRIORITY[hwclock] = "10"
ALTERNATIVE_LINK_NAME[hwclock] = "${base_sbindir}/hwclock"

ALTERNATIVE_util-linux-fdisk = "fdisk"
ALTERNATIVE_LINK_NAME[fdisk] = "${base_sbindir}/fdisk"

ALTERNATIVE_util-linux-agetty = "getty"
ALTERNATIVE_LINK_NAME[getty] = "${base_sbindir}/getty"
ALTERNATIVE_TARGET[getty] = "${base_sbindir}/agetty"

ALTERNATIVE_util-linux-mount = "mount"
ALTERNATIVE_LINK_NAME[mount] = "${base_bindir}/mount"

ALTERNATIVE_util-linux-umount = "umount"
ALTERNATIVE_LINK_NAME[umount] = "${base_bindir}/umount"

ALTERNATIVE_util-linux-losetup = "losetup"
ALTERNATIVE_LINK_NAME[losetup] = "${base_sbindir}/losetup"

ALTERNATIVE_util-linux-swaponoff = "swapoff swapon"
ALTERNATIVE_LINK_NAME[swapoff] = "${base_sbindir}/swapoff"
ALTERNATIVE_LINK_NAME[swapon] = "${base_sbindir}/swapon"

ALTERNATIVE_util-linux-fsck = "fsck.minix fsck"
ALTERNATIVE_LINK_NAME[fsck.minix] = "${base_sbindir}/fsck.minix"
ALTERNATIVE_LINK_NAME[fsck] = "${base_sbindir}/fsck"

ALTERNATIVE_util-linux-blkid = "blkid"
ALTERNATIVE_LINK_NAME[blkid] = "${base_sbindir}/blkid"

ALTERNATIVE_util-linux-reset = "reset"
ALTERNATIVE_LINK_NAME[reset] = "${bindir}/reset"
ALTERNATIVE_TARGET[reset] = "${base_bindir}/reset"

BBCLASSEXTEND = "native"
