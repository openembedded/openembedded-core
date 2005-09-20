DESCRIPTION = "Miscellaneous files for the base system."
SECTION = "base"
PRIORITY = "required"
PR = "r41"
LICENSE = "GPL"

SRC_URI = " \
           file://nsswitch.conf \
           file://motd \
           file://inputrc \
           file://host.conf \
           file://profile \
           file://fstab \
	   file://filesystems \
           file://issue.net \
           file://issue \
           file://usbd \
           file://share/dot.bashrc \
           file://share/dot.profile \
           file://licenses/BSD \
           file://licenses/GPL-2 \
           file://licenses/LGPL-2 \
           file://licenses/LGPL-2.1 \
           file://licenses/Artistic "
S = "${WORKDIR}"

docdir_append = "/${P}"
dirs1777 = "/tmp ${localstatedir}/lock ${localstatedir}/tmp"
dirs2775 = "/home ${prefix}/src ${localstatedir}/local"
dirs755 = "/bin /boot /dev ${sysconfdir} ${sysconfdir}/default \
	   ${sysconfdir}/skel /lib /mnt /proc /home/root /sbin \
	   ${prefix} ${bindir} ${docdir} /usr/games ${includedir} \
	   ${libdir} ${sbindir} ${datadir} \
	   ${datadir}/common-licenses ${datadir}/dict ${infodir} \
	   ${mandir} ${datadir}/misc ${localstatedir} \
	   ${localstatedir}/backups ${localstatedir}/cache \
	   ${localstatedir}/lib /sys ${localstatedir}/lib/misc \
	   ${localstatedir}/lock/subsys ${localstatedir}/log \
	   ${localstatedir}/run ${localstatedir}/spool \
	   /mnt /media /media/card /media/cf /media/net /media/ram \
	   /media/union /media/realroot /media/hdd \
           /media/mmc1"
conffiles = "${sysconfdir}/debian_version ${sysconfdir}/host.conf \
	     ${sysconfdir}/inputrc ${sysconfdir}/issue /${sysconfdir}/issue.net \
	     ${sysconfdir}/nsswitch.conf ${sysconfdir}/profile \
	     ${sysconfdir}/default"

hostname = "openembedded"
hostname_openslug = "openslug"
hostname_mnci = "MNCI"
PACKAGE_ARCH_mnci = "mnci"
hostname_rt3000 = "MNRT"
PACKAGE_ARCH_rt3000 = "rt3000"

do_install () {
	for d in ${dirs755}; do
		install -m 0755 -d ${D}$d
	done
	for d in ${dirs1777}; do
		install -m 1777 -d ${D}$d
	done
	for d in ${dirs2775}; do
		install -m 2755 -d ${D}$d
	done
	for d in card cf net ram; do
		ln -sf /media/$d ${D}/mnt/$d
	done

	if [ -n "${MACHINE}" -a "${hostname}" = "openembedded" ]; then
		echo ${MACHINE} > ${D}${sysconfdir}/hostname
	else
		echo ${hostname} > ${D}${sysconfdir}/hostname
	fi

        if [ -n "${DISTRO_NAME}" ]; then
		echo -n "${DISTRO_NAME} " > ${D}${sysconfdir}/issue
		echo -n "${DISTRO_NAME} " > ${D}${sysconfdir}/issue.net
		if [ -n "${DISTRO_VERSION}" ]; then
			echo -n "${DISTRO_VERSION} " >> ${D}${sysconfdir}/issue
			echo -n "${DISTRO_VERSION} " >> ${D}${sysconfdir}/issue.net
		fi
		echo "\n \l" >> ${D}${sysconfdir}/issue
		echo >> ${D}${sysconfdir}/issue
		echo "%h"    >> ${D}${sysconfdir}/issue.net
		echo >> ${D}${sysconfdir}/issue.net
	else
 	       install -m 0644 ${WORKDIR}/issue ${D}${sysconfdir}/issue
 	       install -m 0644 ${WORKDIR}/issue.net ${D}${sysconfdir}/issue.net
 	fi

	install -m 0644 ${WORKDIR}/fstab ${D}${sysconfdir}/fstab
	install -m 0644 ${WORKDIR}/filesystems ${D}${sysconfdir}/filesystems
	install -m 0644 ${WORKDIR}/usbd ${D}${sysconfdir}/default/usbd
	install -m 0644 ${WORKDIR}/profile ${D}${sysconfdir}/profile
	install -m 0755 ${WORKDIR}/share/dot.profile ${D}${sysconfdir}/skel/.profile
	install -m 0755 ${WORKDIR}/share/dot.bashrc ${D}${sysconfdir}/skel/.bashrc
	install -m 0644 ${WORKDIR}/inputrc ${D}${sysconfdir}/inputrc
	install -m 0644 ${WORKDIR}/nsswitch.conf ${D}${sysconfdir}/nsswitch.conf
	install -m 0644 ${WORKDIR}/host.conf ${D}${sysconfdir}/host.conf
	install -m 0644 ${WORKDIR}/motd ${D}${sysconfdir}/motd

	for license in BSD GPL-2 LGPL-2 LGPL-2.1 Artistic; do
		install -m 0644 ${WORKDIR}/licenses/$license ${D}${datadir}/common-licenses/
	done

	ln -sf /proc/mounts ${D}${sysconfdir}/mtab
}


do_install_append_mnci () {
	rmdir ${D}/tmp
	mkdir -p ${D}${localstatedir}/tmp
	ln -s var/tmp ${D}/tmp
}

do_install_append_nylon() {
	printf "" "" >${D}${sysconfdir}/resolv.conf
	rm -r ${D}/mnt/*
	rm -r ${D}/media
	rm -rf ${D}/tmp
	ln -sf /var/tmp ${D}/tmp
}

do_install_append_openslug() {
	printf "" "" >${D}${sysconfdir}/resolv.conf
	rm -r ${D}/mnt/*
	rmdir ${D}/home/root
	install -m 0755 -d ${D}/root
	ln -s ../root ${D}/home/root
}

PACKAGES = "${PN}-doc ${PN}"
FILES_${PN} = "/"
FILES_${PN}-doc = "${docdir} ${datadir}/common-licenses"


# Unslung distribution specific packages follow ...

PACKAGES_unslung = "${PN}-unslung"
PACKAGE_ARCH_${PN}-unslung = "nslu2"
MAINTAINER_${PN}-unslung = "NSLU2 Linux <www.nslu2-linux.org>"
RDEPENDS_${PN}-unslung = "nslu2-linksys-ramdisk"
RPROVIDES_${PN}-unslung = "${PN}"

FILES_${PN}-unslung = ""

CONFFILES_${PN} = "${sysconfdir}/fstab ${sysconfdir}/hostname"
CONFFILES_${PN}_nylon = "${sysconfdir}/resolv.conf ${sysconfdir}/fstab ${sysconfdir}/hostname"
CONFFILES_${PN}_openslug = "${sysconfdir}/resolv.conf ${sysconfdir}/fstab ${sysconfdir}/hostname"

