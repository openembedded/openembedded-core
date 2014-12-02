SUMMARY = "A suite of basic system administration utilities"
DESCRIPTION = "Util-linux includes a suite of basic system administration utilities \
commonly found on most Linux systems.  Some of the more important utilities include \
disk partitioning, kernel message management, filesystem creation, and system login."

SECTION = "base"

LICENSE = "GPLv2+ & LGPLv2.1+ & BSD"

LIC_FILES_CHKSUM = "file://README.licensing;md5=1715f5ee3e01203ca1e1e0b9ee65918c \
                    file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://Documentation/licenses/COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://Documentation/licenses/COPYING.LGPLv2.1;md5=4fbd65380cdd255951079008b364516c \
                    file://Documentation/licenses/COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af \
                    file://Documentation/licenses/COPYING.UCB;md5=263860f8968d8bafa5392cab74285262 \
                    file://libuuid/COPYING;md5=b442ffb762cf8d3e9df1b99e0bb4af70 \
                    file://libmount/COPYING;md5=fb93f01d4361069c5616327705373b16 \
                    file://libblkid/COPYING;md5=fb93f01d4361069c5616327705373b16"

inherit autotools gettext pkgconfig systemd
DEPENDS = "zlib ncurses"
DEPENDS_append_class-native = " lzo-native"
DEPENDS_append_class-nativesdk = " lzo-native"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/util-linux/v${MAJOR_VERSION}/util-linux-${PV}.tar.xz \
           file://MCONFIG \
           file://defines.h \
           file://make_include \
           file://swapargs.h \
           "

PACKAGES =+ "util-linux-agetty util-linux-fdisk util-linux-cfdisk util-linux-sfdisk \
             util-linux-swaponoff util-linux-losetup util-linux-umount \
             util-linux-mount util-linux-readprofile util-linux-libblkid \
             util-linux-libmount util-linux-libuuid util-linux-uuidd \
             util-linux-uuidgen util-linux-lscpu util-linux-fsck util-linux-blkid \
             util-linux-mkfs util-linux-mcookie util-linux-reset \
             util-linux-mkfs.cramfs util-linux-fsck.cramfs util-linux-fstrim \
             util-linux-partx ${PN}-bash-completion util-linux-hwclock \
             util-linux-findfs"

SHARED_EXTRA_OECONF = "--disable-use-tty-group \
                       --disable-makeinstall-chown \
                       --enable-kill --enable-last --enable-mesg --enable-partx \
                       --enable-raw --enable-reset --disable-login \
                       --disable-vipw --disable-newgrp --disable-chfn-chsh \
                       --enable-write --enable-mount \
                       --enable-libuuid --enable-libblkid --enable-fsck --without-udev \
                       usrsbin_execdir='${sbindir}' \
"

EXTRA_OECONF = "${SHARED_EXTRA_OECONF} --libdir=${base_libdir}"

PACKAGECONFIG_class-target ?= "${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'pam', '', d)} \
                                ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} "
PACKAGECONFIG[pam] = "--enable-su --enable-runuser,--disable-su --disable-runuser, libpam,"

# Respect the systemd feature for uuidd
PACKAGECONFIG[systemd] = "--enable-socket-activation --with-systemdsystemunitdir=${systemd_unitdir}/system/, --disable-socket-activation --without-systemdsystemunitdir"

# Build setpriv requires libcap-ng
PACKAGECONFIG[libcap-ng] = "--enable-setpriv,--disable-setpriv,libcap-ng,"

FILES_${PN}-bash-completion += "${datadir}/bash-completion"
FILES_${PN}-doc += "${datadir}/getopt/getopt-*.*"

FILES_util-linux-agetty = "${base_sbindir}/agetty"
FILES_util-linux-fdisk = "${base_sbindir}/fdisk.${BPN}"
FILES_util-linux-fstrim = "${base_sbindir}/fstrim"
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
FILES_util-linux-partx = "${sbindir}/partx"
FILES_util-linux-hwclock = "${base_sbindir}/hwclock.${BPN}"
FILES_util-linux-findfs = "${sbindir}/findfs"

FILES_util-linux-libblkid = "${base_libdir}/libblkid.so.*"
FILES_util-linux-libmount = "${base_libdir}/libmount.so.*"
FILES_util-linux-libuuid = "${base_libdir}/libuuid.so.*"
FILES_util-linux-lscpu = "${bindir}/lscpu"

FILES_util-linux-fsck = "${base_sbindir}/fsck*"
FILES_util-linux-mkfs = "${sbindir}/mkfs"

FILES_util-linux-fsck.cramfs = "${sbindir}/fsck.cramfs"
FILES_util-linux-mkfs.cramfs = "${sbindir}/mkfs.cramfs"


# Util-linux' blkid replaces the e2fsprogs one
FILES_util-linux-blkid = "${base_sbindir}/blkid*"
RCONFLICTS_util-linux-blkid = "e2fsprogs-blkid"
RREPLACES_util-linux-blkid = "e2fsprogs-blkid"

# reset calls 'tput'
RDEPENDS_util-linux-reset += "ncurses"

RDEPENDS_${PN} = "util-linux-umount util-linux-swaponoff util-linux-losetup"
RRECOMMENDS_${PN} = "util-linux-fdisk util-linux-cfdisk util-linux-sfdisk util-linux-mount util-linux-readprofile util-linux-mkfs "

RRECOMMENDS_${PN}_class-native = ""
RRECOMMENDS_${PN}_class-nativesdk = ""
RDEPENDS_${PN}_class-native = ""
RDEPENDS_${PN}_class-nativesdk = ""

RPROVIDES_${PN}-dev = "util-linux-libblkid-dev util-linux-libmount-dev util-linux-libuuid-dev"

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
        sbinprogs_a="pivot_root hwclock mkswap mkfs.minix fsck.minix losetup swapon swapoff fdisk readprofile fsck blkid blockdev fstrim sulogin switch_root"
        usrbinprogs_a="chfn chsh hexdump last logger mesg newgrp renice utmpdump wall setsid chrt flock getopt"
        binprogs_a="dmesg kill more umount mount login reset su"

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

	rm -f ${D}${bindir}/chkdupexe
}

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"

ALTERNATIVE_${PN}  = "dmesg kill more mkswap blockdev pivot_root switch_root"
ALTERNATIVE_${PN} += "mkfs.minix hexdump last logger mesg renice wall"
ALTERNATIVE_${PN} += "setsid chrt flock utmpdump eject getopt sulogin"

ALTERNATIVE_LINK_NAME[dmesg] = "${base_bindir}/dmesg"
ALTERNATIVE_LINK_NAME[kill] = "${base_bindir}/kill"
ALTERNATIVE_LINK_NAME[more] = "${base_bindir}/more"
ALTERNATIVE_LINK_NAME[mkswap] = "${base_sbindir}/mkswap"
ALTERNATIVE_LINK_NAME[blockdev] = "${base_sbindir}/blockdev"
ALTERNATIVE_LINK_NAME[pivot_root] = "${base_sbindir}/pivot_root"
ALTERNATIVE_LINK_NAME[switch_root] = "${base_sbindir}/switch_root"
ALTERNATIVE_LINK_NAME[mkfs.minix] = "${base_sbindir}/mkfs.minix"
ALTERNATIVE_LINK_NAME[eject] = "${bindir}/eject"
ALTERNATIVE_LINK_NAME[getopt] = "${base_bindir}/getopt"
ALTERNATIVE_LINK_NAME[sulogin] = "${base_sbindir}/sulogin"
ALTERNATIVE_TARGET[getopt] = "${bindir}/getopt"

ALTERNATIVE_${PN}-doc = "mountpoint.1 last.1 mesg.1 wall.1 sulogin.8 utmpdump.1 reset.1"

ALTERNATIVE_LINK_NAME[last.1] = "${mandir}/man1/last.1"
ALTERNATIVE_LINK_NAME[mesg.1] = "${mandir}/man1/mesg.1"
ALTERNATIVE_LINK_NAME[mountpoint.1] = "${mandir}/man1/mountpoint.1"
ALTERNATIVE_LINK_NAME[reset.1] = "${mandir}/man1/reset.1"
ALTERNATIVE_LINK_NAME[sulogin.8] = "${mandir}/man8/sulogin.8"
ALTERNATIVE_LINK_NAME[utmpdump.1] = "${mandir}/man1/utmpdump.1"
ALTERNATIVE_LINK_NAME[wall.1] = "${mandir}/man1/wall.1"

ALTERNATIVE_util-linux-hwclock = "hwclock"
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

ALTERNATIVE_util-linux-readprofile = "readprofile"
ALTERNATIVE_LINK_NAME[readprofile] = "${base_sbindir}/readprofile"

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

BBCLASSEXTEND = "native nativesdk"

python do_package_prepend () {
    if '--enable-su' in d.getVar('EXTRA_OECONF', True).split():
        alt_name = "su"
        d.setVarFlag('ALTERNATIVE_LINK_NAME', alt_name, '%s/%s' % (d.getVar('base_bindir', True), alt_name))
        d.appendVar('ALTERNATIVE_%s' % (d.getVar('PN', True)), ' ' + alt_name)
}
