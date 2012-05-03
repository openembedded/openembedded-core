require libxml2.inc

PR = "r8"

SRC_URI += "file://fix_version_info.patch \
            file://libxml2_fix_for_automake_1.12.patch"

SRC_URI[md5sum] = "8127a65e8c3b08856093099b52599c86"
SRC_URI[sha256sum] = "cda23bc9ebd26474ca8f3d67e7d1c4a1f1e7106364b690d822e009fdc3c417ec"
