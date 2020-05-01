SUMMARY = "The Python Programming Language"
HOMEPAGE = "http://www.python.org"
LICENSE = "PSFv2"
SECTION = "devel/python"
# bump this on every change in contrib/python/generate-manifest-2.7.py
INC_PR = "r1"

LIC_FILES_CHKSUM = "file://LICENSE;md5=203a6dbc802ee896020a47161e759642"

SRC_URI = "http://www.python.org/ftp/python/${PV}/Python-${PV}.tar.xz \
           "

SRC_URI[md5sum] = "fd6cc8ec0a78c44036f825e739f36e5a"
SRC_URI[sha256sum] = "b62c0e7937551d0cc02b8fd5cb0f544f9405bafc9a54d3808ed4594812edef43"

# python recipe is actually python 2.x
# also, exclude pre-releases for both python 2.x and 3.x
UPSTREAM_CHECK_REGEX = "[Pp]ython-(?P<pver>2(\.\d+)+).tar"

CVE_PRODUCT = "python"

# Upstream agreement is that these are not security issues:
# https://bugs.python.org/issue32367
CVE_CHECK_WHITELIST += "CVE-2017-17522"
# https://bugs.python.org/issue32056
CVE_CHECK_WHITELIST += "CVE-2017-18207"

# Windows-only, "It was determined that this is a longtime behavior
# of Python that cannot really be altered at this point."
CVE_CHECK_WHITELIST += "CVE-2015-5652"

# This is not exploitable when glibc has CVE-2016-10739 fixed.
CVE_CHECK_WHITELIST += "CVE-2019-18348"

PYTHON_MAJMIN = "2.7"

inherit autotools pkgconfig

EXTRA_OECONF = "\
  --with-threads \
  --with-pymalloc \
  --without-cxx-main \
  --with-signal-module \
  --enable-shared \
  --enable-ipv6=${@bb.utils.contains('DISTRO_FEATURES', 'ipv6', 'yes', 'no', d)} \
  ac_cv_header_bluetooth_bluetooth_h=no ac_cv_header_bluetooth_h=no \
"

do_configure_prepend() {
	libdirleaf="$(echo ${libdir} | sed -e 's:${prefix}/::')"
	sed -i -e "s:SEDMELIBLEAF:${libdirleaf}:g" \
		${S}/configure.ac
}

do_install_append () {
	sed -i -e 's:${HOSTTOOLS_DIR}/install:install:g' \
		-e 's:${HOSTTOOLS_DIR}/mkdir:mkdir:g' \
		${D}/${libdir}/python${PYTHON_MAJMIN}/_sysconfigdata.py
}
