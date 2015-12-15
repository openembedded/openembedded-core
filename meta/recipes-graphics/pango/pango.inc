SUMMARY = "Framework for layout and rendering of internationalized text"
DESCRIPTION = "Pango is a library for laying out and rendering of text, \
with an emphasis on internationalization. Pango can be used anywhere \
that text layout is needed, though most of the work on Pango so far has \
been done in the context of the GTK+ widget toolkit. Pango forms the \
core of text and font handling for GTK+-2.x."
HOMEPAGE = "http://www.pango.org/"
BUGTRACKER = "http://bugzilla.gnome.org"
SECTION = "libs"
LICENSE = "LGPLv2.0+"

X11DEPENDS = "virtual/libx11 libxft"
DEPENDS = "glib-2.0 fontconfig freetype zlib virtual/libiconv cairo harfbuzz qemu-native"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}"
PACKAGECONFIG[x11] = "--with-xft,--without-xft,${X11DEPENDS}"

BBCLASSEXTEND = "native"
DEPENDS_class-native = "glib-2.0-native cairo-native harfbuzz-native"

inherit gnomebase gtk-doc qemu ptest-gnome upstream-version-is-even

EXTRA_AUTORECONF = ""

# seems to go wrong with default cflags
FULL_OPTIMIZATION_arm = "-O2"

EXTRA_OECONF = "--disable-introspection \
	        --disable-debug \
	        "

LEAD_SONAME = "libpango-1.0*"
LIBV = "1.8.0"

# This binary needs to be compiled for the host architecture.  This isn't pretty!
do_compile_prepend () {
	if ${@base_contains('DISTRO_FEATURES', 'ptest', 'true', 'false', d)}; then
		make CC="${BUILD_CC}" CFLAGS="" AM_CPPFLAGS="$(pkg-config-native --cflags glib-2.0)" gen_all_unicode_LDADD="$(pkg-config-native --libs glib-2.0)" -C ${B}/tests gen-all-unicode
	fi
}

FILES_${PN} = "${bindir}/* ${libdir}/libpango*${SOLIBS}"
FILES_${PN}-dev += "${libdir}/pango/${LIBV}/modules/*.la"

RDEPENDS_${PN}-ptest += "liberation-fonts"

RPROVIDES_${PN} += "pango-modules pango-module-indic-lang \
                    pango-module-basic-fc pango-module-arabic-lang"
