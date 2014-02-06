require libdrm.inc

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "
SRC_URI[md5sum] = "01b75624a5da3a7543923e54c3547a24"
SRC_URI[sha256sum] = "fa693c2f1f61befcefbdcc396673e38481110bac9db610afa4b8afb2be0218c1"

