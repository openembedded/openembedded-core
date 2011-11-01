DESCRIPTION = "Clutter graphics library"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPL"

STDDEPENDS = "virtual/libx11 gtk-doc-native pango glib-2.0 libxfixes gdk-pixbuf libxi"
BASE_CONF = "--disable-gtk-doc ${@get_clutter_fpu_setting(bb, d)}"

DEPENDS = "${STDDEPENDS} virtual/libgl"
EXTRA_OECONF = "${BASE_CONF} --with-flavour=glx"

FILESPATH = "${FILE_DIRNAME}/cogl"

inherit autotools pkgconfig gtk-doc gettext

#check for TARGET_FPU=soft and inform configure of the result so it can disable some floating points
require clutter-fpu.inc

#Fix up some weirdness in the docs
do_configure_prepend() {
        sed -i s:doc/reference/Makefile::g ${S}/configure.ac
        sed -i s:doc::g ${S}/Makefile.am
}
