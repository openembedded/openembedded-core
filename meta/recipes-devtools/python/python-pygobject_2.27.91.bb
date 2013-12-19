SUMMARY = "Python GObject bindings"
SECTION = "devel/python"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=a916467b91076e631dd8edb7424769c7"
DEPENDS = "python python-pygobject-native glib-2.0"
DEPENDS_class-native = "python-native glib-2.0-native"
RDEPENDS_class-native = ""
PR = "r6"

MAJ_VER = "${@d.getVar('PV',1).split('.')[0]}.${@d.getVar('PV',1).split('.')[1]}"

SRC_URI = "${GNOME_MIRROR}/pygobject/${MAJ_VER}/pygobject-${PV}.tar.bz2 \
           file://obsolete_automake_macros.patch \
"

SRC_URI[md5sum] = "2b11a3050264721aac83188224b093a8"
SRC_URI[sha256sum] = "a1dffbe2a8e0d490594554ed8d06f0ee4a371acb6c210e7f35158e9ae77e0df4"
S = "${WORKDIR}/pygobject-${PV}"

FILESPATH = "${FILE_DIRNAME}/python-pygobject:${FILE_DIRNAME}/files"
EXTRA_OECONF += "--disable-introspection"

PARALLEL_MAKEINST = ""

inherit autotools distutils-base pkgconfig

# necessary to let the call for python-config succeed
export BUILD_SYS
export HOST_SYS
export STAGING_INCDIR
export STAGING_LIBDIR

PACKAGES += "${PN}-lib"

RDEPENDS_${PN} += "python-textutils"

FILES_${PN} = "${libdir}/python*"
FILES_${PN}-lib = "${libdir}/lib*.so.*"
FILES_${PN}-dev += "${bindir} ${datadir}"

BBCLASSEXTEND = "native"
