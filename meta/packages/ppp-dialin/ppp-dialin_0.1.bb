SECTION = "console/network"
DESCRIPTION = "Enables PPP dial-in through a serial connection"
MAINTAINER = "Rene Wagner <rw@handhelds.org>"
DEPENDS = "ppp"
RDEPENDS = "ppp"
PR = "r4"
LICENSE = "MIT"

SRC_URI = "file://host-peer \
           file://ppp-dialin"

do_install() {
	install -d ${D}${sysconfdir}/ppp/peers
	install -m 0644 ${WORKDIR}/host-peer ${D}${sysconfdir}/ppp/peers/host

	install -d ${D}${sbindir}
	install -m 0755 ${WORKDIR}/ppp-dialin ${D}${sbindir}
}


pkg_postinst() {
if test "x$D" != "x"; then
	exit 1
else
	adduser --system --home /dev/null --no-create-home --empty-password --ingroup nogroup -s ${sbindir}/ppp-dialin ppp
fi
}

pkg_postrm() {
if test "x$D" != "x"; then
	exit 1
else
	deluser ppp
fi
}
