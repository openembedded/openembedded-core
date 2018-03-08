SUMMARY = "A sophisticated Numeric Processing Package for Python"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause & BSD-2-Clause & PSF & Apache-2.0 & BSD"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=fc53b33304171d132128ebe82ea4a645"

SRCNAME = "numpy"

SRC_URI = "https://github.com/${SRCNAME}/${SRCNAME}/releases/download/v${PV}/${SRCNAME}-${PV}.tar.gz \
           file://0001-Don-t-search-usr-and-so-on-for-libraries-by-default-.patch \
           file://fix_shebang_f2py.patch \
           ${CONFIGFILESURI} "

SRC_URI[md5sum] = "0e09f20f62ab9f8a02cb7bd3fd023482"
SRC_URI[sha256sum] = "8708a775be9a9a457b80a49193c57bd9d51a8a195ed1f1c4b8e89eaf3aa646ee"

UPSTREAM_CHECK_URI = "https://github.com/numpy/numpy/releases"
UPSTREAM_CHECK_REGEX = "(?P<pver>\d+(\.\d+)+)\.tar"

CONFIGFILESURI ?= ""

CONFIGFILESURI_aarch64 = " \
    file://config.h \
    file://_numpyconfig.h \
"
CONFIGFILESURI_arm = " \
    file://config.h \
    file://numpyconfig.h \
"
CONFIGFILESURI_armeb = " \
    file://config.h \
    file://numpyconfig.h \
"
CONFIGFILESURI_mipsarcho32el = " \
    file://config.h \
    file://numpyconfig.h \
"
CONFIGFILESURI_x86 = " \
    file://config.h \
    file://numpyconfig.h \
"
CONFIGFILESURI_x86-64 = " \
    file://config.h \
    file://_numpyconfig.h \
"
CONFIGFILESURI_mipsarcho32eb = " \
    file://config.h \
    file://_numpyconfig.h \
"
CONFIGFILESURI_powerpc = " \
    file://config.h \
    file://_numpyconfig.h \
"
CONFIGFILESURI_powerpc64 = " \
    file://config.h \
    file://_numpyconfig.h \
"
CONFIGFILESURI_mipsarchn64eb = " \
    file://config.h \
    file://_numpyconfig.h \
"
CONFIGFILESURI_mipsarchn64el = " \
    file://config.h \
    file://_numpyconfig.h \
"
CONFIGFILESURI_mipsarchn32eb = " \
    file://config.h \
    file://_numpyconfig.h \
"
CONFIGFILESURI_mipsarchn32el = " \
    file://config.h \
    file://_numpyconfig.h \
"

S = "${WORKDIR}/numpy-${PV}"

inherit setuptools

# Make the build fail and replace *config.h with proper one
# This is a ugly, ugly hack - Koen
do_compile_prepend_class-target() {
    ${STAGING_BINDIR_NATIVE}/python-native/python setup.py build ${DISTUTILS_BUILD_ARGS} || \
    true
    cp ${WORKDIR}/*config.h ${S}/build/$(ls ${S}/build | grep src)/numpy/core/include/numpy/
}

FILES_${PN}-staticdev += "${PYTHON_SITEPACKAGES_DIR}/numpy/core/lib/*.a"

# install what is needed for numpy.test()
RDEPENDS_${PN} = "python-unittest \
                  python-difflib \
                  python-pprint \
                  python-pickle \
                  python-shell \
                  python-nose \
                  python-doctest \
                  python-datetime \
                  python-distutils \
                  python-misc \
                  python-mmap \
                  python-netclient \
                  python-numbers \
                  python-pydoc \
                  python-pkgutil \
                  python-email \
                  python-subprocess \
                  python-compression \
                  python-ctypes \
                  python-threading \
"

RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native nativesdk"
