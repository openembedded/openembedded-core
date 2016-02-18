require findutils.inc

# GPLv2+ (<< 4.2.32), GPLv3+ (>= 4.2.32)
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949"
 
DEPENDS = "bison-native"

SRC_URI += "file://0001-Unset-need_charset_alias-when-building-for-musl.patch"

SRC_URI[md5sum] = "1428516452520b51cf893c05500eda3d"
SRC_URI[sha256sum] = "e9bc769d78573c91b1d4e504ad39621b870db6fa38fde923acf6896399f16f8e"

# http://savannah.gnu.org/bugs/?27299
CACHED_CONFIGUREVARS += "${@bb.utils.contains('DISTRO_FEATURES', 'libc-posix-clang-wchar', 'gl_cv_func_wcwidth_works=yes', '', d)}"

EXTRA_OECONF += "ac_cv_path_SORT=${bindir}/sort"
