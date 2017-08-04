SUMMARY = "A sophisticated Numeric Processing Package for Python"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=1002b09cd654fcaa2dcc87535acd9a96"

SRCNAME = "numpy"

SRC_URI = "https://github.com/${SRCNAME}/${SRCNAME}/releases/download/v${PV}/${SRCNAME}-${PV}.tar.gz \
           file://0001-Don-t-search-usr-and-so-on-for-libraries-by-default-.patch \
           file://remove-build-path-in-comments.patch \
           file://fix_shebang_f2py.patch \
           file://0001-BUG-fix-infinite-loop-when-creating-np.pad-on-an-emp.patch \
           ${CONFIGFILESURI} "
SRC_URI[md5sum] = "c1d433e5973e548809e80c9118474b73"
SRC_URI[sha256sum] = "4c6b4eef790528bebb7ec9590d74cc193868940fe68e4109a91c196df72d8094"

UPSTREAM_CHECK_URI = "https://github.com/numpy/numpy/releases"

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

inherit setuptools3

# Make the build fail and replace *config.h with proper one
# This is a ugly, ugly hack - Koen
do_compile_prepend_class-target() {
    ${STAGING_BINDIR_NATIVE}/python3-native/python3 setup.py build ${DISTUTILS_BUILD_ARGS} || \
    true
    cp ${WORKDIR}/*config.h ${S}/build/$(ls ${S}/build | grep src)/numpy/core/include/numpy/
}

FILES_${PN}-staticdev += "${PYTHON_SITEPACKAGES_DIR}/numpy/core/lib/*.a"

# install what is needed for numpy.test()
RDEPENDS_${PN} = "python3-unittest \
                  python3-difflib \
                  python3-pprint \
                  python3-pickle \
                  python3-shell \
                  python3-nose \
                  python3-doctest \
                  python3-datetime \
                  python3-distutils \
                  python3-misc \
                  python3-mmap \
                  python3-netclient \
                  python3-numbers \
                  python3-pydoc \
                  python3-pkgutil \
                  python3-email \
                  python3-compression \
                  python3-ctypes \
                  python3-threading \
"

RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native nativesdk"
