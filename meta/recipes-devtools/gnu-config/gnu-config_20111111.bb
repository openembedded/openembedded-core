SUMMARY = "gnu-configize"
DESCRIPTION = "Tool that installs the GNU config.guess / config.sub into a directory tree"
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://config.guess;endline=39;md5=a3669d051b3a8408d69751e53b2e1cc1"

DEPENDS_virtclass-native = "perl-native-runtime"

INHIBIT_DEFAULT_DEPS = "1"

PR = "r1"

SRC_URI = "http://downloads.yoctoproject.org/releases/gnu-config/gnu-config-yocto-${PV}.tgz \
	   file://config-guess-uclibc.patch \
           file://gnu-configize.in"

SRC_URI[md5sum] = "30be385c919a25cd9522205ef49e5328"
SRC_URI[sha256sum] = "0750afa8d8ee988b6ead1c2d02b565597f809e2e3ad14886ed7803d3bbc8b0cd"

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

BBCLASSEXTEND = "native nativesdk"
