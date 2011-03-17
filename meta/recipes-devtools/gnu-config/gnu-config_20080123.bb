SUMMARY = "gnu-configize"
DESCRIPTION = "Tool that installs the GNU config.guess / config.sub into a directory tree"
SECTION = "devel"
LICENSE = "GPLv1+"
LIC_FILES_CHKSUM = "file://config.guess;endline=39;md5=a089987af4a25cb0419d1c2fd6d495e3"
DEPENDS = ""
INHIBIT_DEFAULT_DEPS = "1"

FIXEDSRCDATE = "${@bb.data.getVar('FILE', d, 1).split('_')[-1].split('.')[0]}"
PV = "0.1+cvs${FIXEDSRCDATE}"
PR = "r2"

SRC_URI = "cvs://anonymous@cvs.sv.gnu.org/cvsroot/config;module=config;method=pserver;date=${FIXEDSRCDATE} \
	   file://config-guess-uclibc.patch \
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
		    -e 's,@autom4te_perllibdir@,${datadir}/autoconf,g' > ${D}${bindir}/gnu-configize
	# In the native case we want the system perl as perl-native can't have built yet
	if [ "${BUILD_ARCH}" != "${TARGET_ARCH}" ]; then
		sed -i -e 's,/usr/bin/env,${bindir}/env,g' ${D}${bindir}/gnu-configize
	fi
	chmod 755 ${D}${bindir}/gnu-configize
	install -m 0644 config.guess config.sub ${D}${datadir}/gnu-config/
}

PACKAGES = "${PN}"
FILES_${PN} = "${bindir} ${datadir}/gnu-config"

BBCLASSEXTEND = "native"
