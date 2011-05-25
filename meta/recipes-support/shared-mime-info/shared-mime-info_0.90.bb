require shared-mime-info.inc
PR = "r0"

SRC_URI += "file://fix-parallel-build.patch \
            file://fix-parallel-build-backport.patch \
           "
SRC_URI[md5sum] = "967d68d3890ba3994cfce3adf5b8f15b"
SRC_URI[sha256sum] = "52c9f84a8c72de631a0458542980b1728560f59845eb5e93e1dbe825f4b72304"
