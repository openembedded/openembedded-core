SUMMARY = "Library providing simplified C and Python API to libsolv"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

SRC_URI = "git://github.com/rpm-software-management/libdnf \
           file://0001-FindGtkDoc.cmake-drop-the-requirement-for-GTKDOC_SCA.patch \
           file://0002-Prefix-sysroot-path-to-introspection-tools-path.patch \
           file://0003-Set-the-library-installation-directory-correctly.patch \
           file://0004-Set-libsolv-variables-with-pkg-config-cmake-s-own-mo.patch \
           file://0001-Get-parameters-for-both-libsolv-and-libsolvext-libdn.patch \
           "

PV = "0.2.3+git${SRCPV}"
SRCREV = "367545629cc01a8e622890d89bd13d062ce60d7b"

S = "${WORKDIR}/git"

DEPENDS = "glib-2.0 libsolv libcheck librepo rpm gtk-doc"

inherit gtk-doc gobject-introspection cmake pkgconfig

# We cannot inherit pythonnative (or descendant classes like distutils etc.) 
# because that would conflict with inheriting python3native
# (which is done by inheriting gobject-introspection). 
# But libdnf only needs the path to native Python 2.x binary
# so we simply set it explicitly here.
#
# These lines can be dropped when dnf stack is moved to python 3.x
EXTRANATIVEPATH += "python-native"
FILES_${PN} += " ${libdir}/python2.7/*"
DEPENDS += "python-native"

EXTRA_OECMAKE = " -DPYTHON_INSTALL_DIR=${PYTHON_SITEPACKAGES_DIR} -DWITH_MAN=OFF \
                  ${@bb.utils.contains('GI_DATA_ENABLED', 'True', '-DWITH_GIR=ON', '-DWITH_GIR=OFF', d)} \
                "
EXTRA_OECMAKE_append_class-native = " -DWITH_GIR=OFF"
EXTRA_OECMAKE_append_class-nativesdk = " -DWITH_GIR=OFF"

BBCLASSEXTEND = "native"

