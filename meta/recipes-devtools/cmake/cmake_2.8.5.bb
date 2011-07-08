require cmake.inc

inherit cmake

DEPENDS += "curl expat zlib libarchive ncurses"

PR = "${INC_PR}.0"

# This was need to keep version consistent - will be removed once 2.8.5 is released
SRC_URI = "http://www.cmake.org/files/v${CMAKE_MAJOR_VERSION}/cmake-2.8.5-rc3.tar.gz \
           file://support-oe-qt4-tools-names.patch"
S = "${WORKDIR}/cmake-2.8.5-rc3"

SRC_URI += "file://dont-run-cross-binaries.patch"

SRC_URI[md5sum] = "2d8018f8fa4c499e2c5b288d71660cba"
SRC_URI[sha256sum] = "2987befc451f6404ea93bb99f00a38b80724fb655f121fed3bb0a08b65a771c8"

# Strip ${prefix} from ${docdir}, set result into docdir_stripped
python () {
    prefix=bb.data.getVar("prefix", d, 1)
    docdir=bb.data.getVar("docdir", d, 1)

    if not docdir.startswith(prefix):
	raise bb.build.FuncFailed('docdir must contain prefix as its prefix')

    docdir_stripped = docdir[len(prefix):]
    if len(docdir_stripped) > 0 and docdir_stripped[0] == '/':
	docdir_stripped = docdir_stripped[1:]

    bb.data.setVar("docdir_stripped", docdir_stripped, d)
}

EXTRA_OECMAKE=" \
    -DCMAKE_DOC_DIR=${docdir_stripped}/cmake-${CMAKE_MAJOR_VERSION} \
    -DCMAKE_USE_SYSTEM_LIBRARIES=1 \
    -DKWSYS_CHAR_IS_SIGNED=1 \
    ${@base_contains('DISTRO_FEATURES', 'largefile', '-DKWSYS_LFS_WORKS=1', '-DKWSYS_LFS_DISABLE=1', d)} \
"

# FIXME: Hack due gcc-crosssdk not being able to detect those automatically
CXXFLAGS_virtclass-nativesdk += " \
   -I${STAGING_DIR_HOST}${SDKPATHNATIVE}/usr/include/c++ \
   -I${STAGING_DIR_HOST}${SDKPATHNATIVE}/usr/include/c++/${TARGET_SYS} \
   "

FILES_${PN} += "${datadir}/cmake-${CMAKE_MAJOR_VERSION}"
FILES_${PN}-doc += "${docdir}/cmake-${CMAKE_MAJOR_VERSION}"

BBCLASSEXTEND = "nativesdk"
