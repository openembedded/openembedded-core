require cmake.inc

inherit cmake

DEPENDS += "curl expat zlib libarchive ncurses"

SRC_URI += "file://dont-run-cross-binaries.patch"

SRC_URI[md5sum] = "df5324a3b203373a9e0a04b924281a43"
SRC_URI[sha256sum] = "b7dbb86824051319b8e082d2f892ebe6d5914b4dac9b9ef1aeac5e7ed054b0bf"

# Strip ${prefix} from ${docdir}, set result into docdir_stripped
python () {
    prefix=d.getVar("prefix", True)
    docdir=d.getVar("docdir", True)

    if not docdir.startswith(prefix):
        raise bb.build.FuncFailed('docdir must contain prefix as its prefix')

    docdir_stripped = docdir[len(prefix):]
    if len(docdir_stripped) > 0 and docdir_stripped[0] == '/':
        docdir_stripped = docdir_stripped[1:]

    d.setVar("docdir_stripped", docdir_stripped)
}

EXTRA_OECMAKE=" \
    -DCMAKE_DOC_DIR=${docdir_stripped}/cmake-${CMAKE_MAJOR_VERSION} \
    -DCMAKE_USE_SYSTEM_LIBRARIES=1 \
    -DKWSYS_CHAR_IS_SIGNED=1 \
    -DBUILD_CursesDialog=0 \
    ${@base_contains('DISTRO_FEATURES', 'largefile', '-DKWSYS_LFS_WORKS=1', '-DKWSYS_LFS_DISABLE=1', d)} \
"

FILES_${PN} += "${datadir}/cmake-${CMAKE_MAJOR_VERSION}"
FILES_${PN}-doc += "${docdir}/cmake-${CMAKE_MAJOR_VERSION}"

BBCLASSEXTEND = "nativesdk"
