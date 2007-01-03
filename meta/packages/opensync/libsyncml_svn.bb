DESCRIPTION = "Libsyncml is a implementation of the SyncML protocol."
HOMEPAGE = "http://libsyncml.opensync.org/"
LICENSE = "LGPL"

DEPENDS = "sed-native wbxml2 libsoup libxml2 bluez-libs openobex"

PV = "0.4.2+svn${SRCDATE}"
SRC_URI = "svn://svn.opensync.org/libsyncml;module=trunk;proto=http"
S = "${WORKDIR}/trunk"

inherit autotools pkgconfig

EXTRA_OECONF = " --enable-http \
  		 --enable-obex \
  		 --enable-bluetooth \
    		 --enable-tools \
		 --with-wbxml"

CFLAGS += "-I${STAGING_INCDIR}/libsoup-2.2"

do_configure_append() {
        sed -i s:-I/usr/include/:-I/foo/:g Makefile
        sed -i s:-I/usr/include/:-I/foo/:g */Makefile
	sed -i s:-I/usr/include/:-I/foo/:g */*/Makefile
}

do_stage() {
        autotools_stage_all
}

PACKAGES += "${PN}-tools"

FILES_${PN}-tools = "${bindir}"
FILES_${PN} = "${libdir}/*.so.*"

