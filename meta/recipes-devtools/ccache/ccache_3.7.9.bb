SUMMARY = "a fast C/C++ compiler cache"
DESCRIPTION = "ccache is a compiler cache. It speeds up recompilation \
by caching the result of previous compilations and detecting when the \
same compilation is being done again. Supported languages are C, C\+\+, \
Objective-C and Objective-C++."
HOMEPAGE = "http://ccache.samba.org"
SECTION = "devel"

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://LICENSE.adoc;md5=22d514dbc01fdf9a9784334b6b59417a"

DEPENDS = "zlib"

SRC_URI = "https://github.com/ccache/ccache/releases/download/v${PV}/${BP}.tar.gz"
SRC_URI[md5sum] = "a4a38afc62ed189904357739fd8f3fb8"
SRC_URI[sha256sum] = "92838e2133c9e704fdab9ee2608dad86c99021278b9ac47d065aa8ff2ea8ce36"

UPSTREAM_CHECK_URI = "https://github.com/ccache/ccache/releases/"

inherit autotools

# Remove ccache-native's dependencies, so that it can be used widely by
# other native recipes.
DEPENDS_class-native = ""
EXTRA_OECONF_class-native = "--with-bundled-zlib"
INHIBIT_AUTOTOOLS_DEPS_class-native = "1"
PATCHTOOL = "patch"

BBCLASSEXTEND = "native"

do_configure_class-native() {
    oe_runconf
}
