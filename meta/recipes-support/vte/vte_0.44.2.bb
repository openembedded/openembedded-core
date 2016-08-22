SUMMARY = "Virtual terminal emulator GTK+ widget library"
BUGTRACKER = "https://bugzilla.gnome.org/buglist.cgi?product=vte"
LICENSE = "LGPLv2.1+"
DEPENDS = "glib-2.0 gtk+3 intltool-native"

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

inherit gnomebase gtk-doc distro_features_check upstream-version-is-even vala gobject-introspection

SRC_URI += "file://0001-Don-t-enable-stack-protection-by-default.patch"
SRC_URI[archive.md5sum] = "eca8f8a9d9f9bb8e9d592d0acfeec015"
SRC_URI[archive.sha256sum] = "a1ea594814bb136a3a9a6c7656b46240571f6a198825c1111007fe99194b0949"

ANY_OF_DISTRO_FEATURES = "${GTK3DISTROFEATURES}"

PACKAGECONFIG[gnutls] = "--with-gnutls,--without-gnutls,gnutls"

CFLAGS += "-D_GNU_SOURCE"

# Enable vala only if gobject-introspection is enabled
EXTRA_OECONF = "--enable-vala=auto --disable-test-application"

# libtool adds "-nostdlib" when g++ is used. This breaks PIE builds.
# Use libtool-cross (which has a hack to prevent that) instead.
EXTRA_OEMAKE_class-target = "LIBTOOL=${STAGING_BINDIR_CROSS}/${HOST_SYS}-libtool"

PACKAGES =+ "libvte"
FILES_libvte = "${libdir}/*.so.*"
