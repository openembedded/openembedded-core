require findutils.inc

# GPLv2+ (<< 4.2.32), GPLv3+ (>= 4.2.32)
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949"
 
PR = "r6"

SRC_URI += "file://01-27017.patch \
            file://02-28824.patch \
            file://03-28872.patch \
            file://findutils_fix_for_x32.patch \
            file://findutils_fix_for_automake-1.12.patch \
            file://findutils_fix_doc.patch \
            "

SRC_URI[md5sum] = "351cc4adb07d54877fa15f75fb77d39f"
SRC_URI[sha256sum] = "434f32d171cbc0a5e72cfc5372c6fc4cb0e681f8dce566a0de5b6fccd702b62a"

DEPENDS = "bison-native"

# http://savannah.gnu.org/bugs/?27299
CACHED_CONFIGUREVARS += "${@bb.utils.contains('DISTRO_FEATURES', 'libc-posix-clang-wchar', 'gl_cv_func_wcwidth_works=yes', '', d)}"

EXTRA_OECONF += "ac_cv_path_SORT=${bindir}/sort"
