SUMMARY = "Provides a way to load and enumerate PKCS#11 modules"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=02933887f609807fbb57aa4237d14a50"

inherit meson gettext pkgconfig gtk-doc

DEPENDS = "libtasn1 libtasn1-native libffi"

DEPENDS_append = "${@' glib-2.0' if d.getVar('GTKDOC_ENABLED') == 'True' else ''}"

SRC_URI = "git://github.com/p11-glue/p11-kit"
SRCREV = "b0ebe7555c291808db29377ba79cb8326301f0a6"
S = "${WORKDIR}/git"

PACKAGECONFIG ??= ""
PACKAGECONFIG[trust-paths] = "-Dtrust_paths=/etc/ssl/certs/ca-certificates.crt,,,ca-certificates"

GTKDOC_MESON_OPTION = 'gtk_doc'

FILES_${PN} += " \
    ${libdir}/p11-kit-proxy.so \
    ${libdir}/pkcs11/*.so \
    ${libdir}/pkcs11/*.la"

# PN contains p11-kit-proxy.so, a symlink to a loadable module
INSANE_SKIP_${PN} = "dev-so"
