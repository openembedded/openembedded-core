SECTION = "base"
DESCRIPTION = "gnu-configize"
LICENSE = "GPL"
DEPENDS = ""
INHIBIT_DEFAULT_DEPS = "1"
MAINTAINER = "Chris Larson <kergoth@handhelds.org>"

FIXEDCVSDATE = "${@bb.data.getVar('FILE', d, 1).split('_')[-1].split('.')[0]}"
PV = "0.1cvs${FIXEDCVSDATE}"
PR = "r4"

SRC_URI = "cvs://anoncvs:@savannah.gnu.org/cvsroot/config;module=config;method=ext;rsh=ssh;date=${FIXEDCVSDATE} \
	   file://config-guess-uclibc.patch;patch=1 \
           file://gnu-configize.in"
S = "${WORKDIR}/config"

do_compile() {
	:
}

do_install () {
	install -d ${D}${datadir}/gnu-config \
		   ${D}${bindir}
	cat ${WORKDIR}/gnu-configize.in | \
		sed -e 's,@gnu-configdir@,${datadir}/gnu-config,g' \
		    -e 's,@autom4te_perllibdir@,${datadir}/autoconf,g' \
		    -e 's,/usr/bin/perl,${bindir}/perl,g' > ${D}${bindir}/gnu-configize
	chmod 755 ${D}${bindir}/gnu-configize
	install -m 0644 config.guess config.sub ${D}${datadir}/gnu-config/
}

PACKAGES = "${PN}"
FILES_${PN} = "${bindir} ${datadir}/gnu-config"
