DESCRIPTION = "Network suspend/resume scripts"
SECTION = "base"
LICENSE = "GPL"
PV = "1.1"
PR = "r2"

SRC_URI = "file://ifupdown \
           file://usbnet"  

do_install() {
	install -d ${D}${sysconfdir}/apm/scripts.d
	install -m 0755 ${WORKDIR}/ifupdown ${D}${sysconfdir}/apm/scripts.d
	install -m 0755 ${WORKDIR}/usbnet ${D}${sysconfdir}/apm/scripts.d
	install -d ${D}${sysconfdir}/apm/suspend.d
	ln -s ../scripts.d/ifupdown ${D}${sysconfdir}/apm/suspend.d/05ifupdown
	ln -s ../scripts.d/usbnet ${D}${sysconfdir}/apm/suspend.d/10usbnet
	install -d ${D}${sysconfdir}/apm/resume.d
	ln -sf ../scripts.d/ifupdown ${D}${sysconfdir}/apm/resume.d/30ifupdown
	ln -sf ../scripts.d/usbnet ${D}${sysconfdir}/apm/resume.d/20usbnet
}

FILES = "${sysconfdir}/apm"
