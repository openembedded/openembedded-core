LICENSE = "GPL"
DEPENDS = "openssl"

SRC_URI = "http://www.tildeslash.com/monit/dist/monit-${PV}.tar.gz\
	file://init"

INITSCRIPT_NAME = "monit"
INITSCRIPT_PARAMS = "defaults 99"

inherit autotools update-rc.d

EXTRA_OECONF = "--with-ssl-lib-dir=${STAGING_LIBDIR} --with-ssl-incl-dir=${STAGING_INCDIR}" 

do_install_append() {
	install -d ${D}${sysconfdir}/init.d/
	install -m 755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/monit
	sed -i 's:# set daemon  120:set daemon  120:' ${S}/monitrc
	sed -i 's:include /etc/monit.d/:include /${sysconfdir}/monit.d/:' ${S}/monitrc
	install -m 600 ${S}/monitrc ${D}${sysconfdir}/monitrc
	install -m 700 -d ${D}${sysconfdir}/monit.d/
}

CONFFILES_${PN} += "${sysconfdir}/monitrc"

