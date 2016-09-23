SECTION = "devel"
SUMMARY = "Linux Trace Toolkit Control"
DESCRIPTION = "The Linux trace toolkit is a suite of tools designed \
to extract program execution details from the Linux operating system \
and interpret them."

LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=01d7fc4496aacf37d90df90b90b0cac1 \
                    file://gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://lgpl-2.1.txt;md5=0f0d71500e6a57fd24d825f33242b9ca"

DEPENDS = "liburcu popt libxml2 util-linux"
RDEPENDS_${PN} = "libgcc"
RDEPENDS_${PN}-ptest += "make perl bash"

SRCREV = "d11e0dba0df9024b8613c51e167a379b91e8b20b"
PV = "2.8.1+git${SRCPV}"

PYTHON_OPTION = "am_cv_python_pyexecdir='${PYTHON_SITEPACKAGES_DIR}' \
                 am_cv_python_pythondir='${PYTHON_SITEPACKAGES_DIR}' \
                 PYTHON_INCLUDE='-I${STAGING_INCDIR}/python${PYTHON_BASEVERSION}${PYTHON_ABI}' \
"
PACKAGECONFIG ??= "lttng-ust"
PACKAGECONFIG[python] = "--enable-python-bindings ${PYTHON_OPTION},,python3 swig-native"
PACKAGECONFIG[lttng-ust] = "--with-lttng-ust, --without-lttng-ust, lttng-ust"
PACKAGECONFIG[kmod] = "--enable-kmod, --disable-kmod, kmod"
PACKAGECONFIG[manpages] = "--enable-man-pages, --disable-man-pages, asciidoc-native"
PACKAGECONFIG_remove_libc-musl = "lttng-ust"

SRC_URI = "git://git.lttng.org/lttng-tools.git;branch=stable-2.8 \
           file://0001-Fix-error.h-common-error.h.patch \
           file://runtest-2.4.0.patch \
           file://run-ptest"

S = "${WORKDIR}/git"

inherit autotools-brokensep ptest pkgconfig useradd python3-dir

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "tracing"

FILES_${PN} += "${libdir}/lttng/libexec/* ${datadir}/xml/lttng \
                ${PYTHON_SITEPACKAGES_DIR}/*"
FILES_${PN}-staticdev += "${PYTHON_SITEPACKAGES_DIR}/*.a"
FILES_${PN}-dev += "${PYTHON_SITEPACKAGES_DIR}/*.la"

# Since files are installed into ${libdir}/lttng/libexec we match 
# the libexec insane test so skip it.
# Python module needs to keep _lttng.so
INSANE_SKIP_${PN} = "libexec dev-so"
INSANE_SKIP_${PN}-dbg = "libexec"

do_configure_prepend () {
	# Delete a shipped m4 file that overrides our patched one
	rm -f ${S}/m4/libxml.m4
}

do_install_ptest () {
	chmod +x ${D}${PTEST_PATH}/tests/utils/utils.sh
	for i in `find ${D}${PTEST_PATH} -perm /u+x -type f`; do
		sed -e "s:\$TESTDIR.*/src/bin/lttng/\$LTTNG_BIN:\$LTTNG_BIN:g" \
		  -e "s:\$TESTDIR/../src/bin/lttng-sessiond/\$SESSIOND_BIN:\$SESSIOND_BIN:g" \
		  -e "s:\$DIR/../src/bin/lttng-sessiond/\$SESSIOND_BIN:\$SESSIOND_BIN:g" \
		  -e "s:\$TESTDIR/../src/bin/lttng-consumerd/:${libdir}/lttng/libexec/:g" \
		  -e "s:\$DIR/../src/bin/lttng-consumerd/:${libdir}/lttng/libexec/:g" \
		  -e "s:\$TESTDIR/../src/bin/lttng-relayd/\$RELAYD_BIN:\$RELAYD_BIN:g" \
		  -e "s:\$DIR/../src/bin/lttng-sessiond/lttng-sessiond:\$SESSIOND_BIN:g" \
		  -e "s:\$DIR/../src/bin/lttng-relayd/\$RELAYD_BIN:\$RELAYD_BIN:g" \
		  -e "s:\$DIR/../bin/lttng-relayd/\$RELAYD_BIN:\$RELAYD_BIN:g" \
		  -i $i
	done

	# Update libdir references in copied .la files
	for i in `find ${D}${PTEST_PATH} -type f -name *.la`; do
		sed -i -e 's#${S}/tests/#${STAGING_DIR}${PTEST_PATH}#g' $i
	done

	sed -e "s:src/bin:bin:g" -e "s:lt-::g" \
	-i ${D}${PTEST_PATH}/tests/utils/utils.sh
	sed -e "s:ini_config:\.libs\/ini_config:" \
	-i ${D}${PTEST_PATH}/tests/unit/ini_config/test_ini_config
}
