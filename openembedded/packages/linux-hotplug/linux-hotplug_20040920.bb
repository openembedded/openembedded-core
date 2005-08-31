DESCRIPTION = "This package contains the scripts necessary \
for hotplug Linux support, and lets you plug in new devices \
and use them immediately."
HOMEPAGE = "http://linux-hotplug.sourceforge.net/"
LICENSE = "GPL"
RPROVIDES_${PN} = "hotplug"
RCONFLICTS_${PN} = "hotplug"
RREPLACES_${PN} = "hotplug"
SECTION = "base"
RSUGGESTS = "pciutils usbutils"
PR = "r7"

SRC_URI = "ftp://ftp.kernel.org/pub/linux/utils/kernel/hotplug/hotplug-2004_09_20.tar.gz \
	file://userspecified_hcd.patch;patch=1 \
	file://hotplug-net-agent-usb.patch;patch=1 \
	file://usbrc-busybox.patch;patch=1 \
	file://fix-net.agent \
	file://update-usb.usermap \
	file://logcheck-ignore \
	file://sleeve.agent \
	file://sleeve.rc \
	file://mmc.agent \
	file://usbd.agent \
	file://usbd \
	file://sysconfig-hotplug \
	file://sysconfig-usb \
	file://isapnp-exit.diff;patch=1 \
	file://usb-storage \
	file://soc.agent"
	   
S = "${WORKDIR}/hotplug-2004_09_20"

INITSCRIPT_NAME = "hotplug"
INITSCRIPT_PARAMS = "start 40 S . stop 89 0 6 ."

inherit update-rc.d

do_compile () {
	:
}

oldmandir := "${mandir}"
oldsbindir := "${sbindir}"
prefix = ""
exec_prefix = ""
FILES_${PN} += "${oldsbindir}"
FILES_${PN}-doc += "${oldmandir}"

export DEBFIX = "sed -e 's:sysconfig/usb:default/hotplug.usb:'"
do_install () {
	install -d ${D}${sysconfdir}/logcheck/ignore.d
	install -d ${D}${oldmandir}
	install -d ${D}${oldsbindir}
	#install -d ${D}${sysconfdir}/default
	oe_runmake prefix=${D}${prefix} exec_prefix=${D}${exec_prefix} \
		   etcdir=${D}${sysconfdir} sbindir=${D}${sbindir} \
		   mandir=${D}${oldmandir} INSTALL=install install
	sh ${WORKDIR}/fix-net.agent ${D} || :
	install -m 0755 ${WORKDIR}/update-usb.usermap ${D}${oldsbindir}/
	install -m 0644 ${WORKDIR}/logcheck-ignore ${D}${sysconfdir}/logcheck/ignore.d/hotplug
	install -m 0755 ${WORKDIR}/sleeve.agent ${D}${sysconfdir}/hotplug/
	install -m 0755 ${WORKDIR}/sleeve.rc ${D}${sysconfdir}/hotplug/
	install -m 0755 ${WORKDIR}/mmc.agent ${D}${sysconfdir}/hotplug/
	install -m 0755 ${WORKDIR}/usbd.agent ${D}${sysconfdir}/hotplug/
	install -m 0755 ${WORKDIR}/soc.agent ${D}${sysconfdir}/hotplug/
	#install -m 0755 ${WORKDIR}/usbd ${D}${sysconfdir}/default/usbd
	install -d ${D}${sysconfdir}/sysconfig
	install -m 0644 ${WORKDIR}/sysconfig-hotplug ${D}${sysconfdir}/sysconfig/hotplug
	install -m 0644 ${WORKDIR}/sysconfig-usb ${D}${sysconfdir}/sysconfig/usb
	install -m 0755 ${WORKDIR}/usb-storage ${D}${sysconfdir}/hotplug/usb
}
