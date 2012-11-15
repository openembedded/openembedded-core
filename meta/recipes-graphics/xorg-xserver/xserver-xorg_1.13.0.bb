require xserver-xorg.inc

# Misc build failure for master HEAD
SRC_URI += "file://crosscompile.patch \
            file://fix_open_max_preprocessor_error.patch \
            file://mips64-compiler.patch \
           "

SRC_URI[md5sum] = "bde3d178b756597d2ec2a19ef60d2e1f"
SRC_URI[sha256sum] = "548b91cb01087ad4f892428e7ac4955251cf12393e86c70420436b33848ef28a"

PR = "${INC_PR}.0"
