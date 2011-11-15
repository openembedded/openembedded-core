SUMMARY = "gnu-configize"
DESCRIPTION = "Tool that installs the GNU config.guess / config.sub into a directory tree"
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://config.guess;endline=39;md5=0e6ca0501b27177f3bc640f7225e3ead"

DEPENDS_virtclass-native = "perl-native-runtime"

INHIBIT_DEFAULT_DEPS = "1"

SRCREV = "a47f842264fc19837f8a00eb1d2d254a4c527334"
PV = "1.0+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.sv.gnu.org/config.git;protocol=git \
	   file://config-guess-uclibc.patch \
           file://gnu-configize.in"

S = "${WORKDIR}/git"

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
