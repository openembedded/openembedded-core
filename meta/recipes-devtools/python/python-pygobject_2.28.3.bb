SUMMARY = "Python GObject bindings"
SECTION = "devel/python"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=a916467b91076e631dd8edb7424769c7"
DEPENDS = "python python-pygobject-native libffi glib-2.0"
DEPENDS_class-native = "python-native libffi-native glib-2.0-native"
RDEPENDS_class-native = ""

MAJ_VER = "${@d.getVar('PV',1).split('.')[0]}.${@d.getVar('PV',1).split('.')[1]}"

SRC_URI = "${GNOME_MIRROR}/pygobject/${MAJ_VER}/pygobject-${PV}.tar.bz2 \
           file://obsolete_automake_macros.patch \
"

SRC_URI[md5sum] = "aa64900b274c4661a5c32e52922977f9"
SRC_URI[sha256sum] = "7da88c169a56efccc516cebd9237da3fe518a343095a664607b368fe21df95b6"
S = "${WORKDIR}/pygobject-${PV}"

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
