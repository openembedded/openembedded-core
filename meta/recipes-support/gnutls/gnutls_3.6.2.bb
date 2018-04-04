require gnutls.inc

SRC_URI += "file://0001-configure.ac-fix-sed-command.patch \
            file://arm_eabi.patch \
           "
SRC_URI[md5sum] = "8b4912c6c0e5ffefd3dbb4888eaf8a58"
SRC_URI[sha256sum] = "bcd5db7b234e02267f36b5d13cf5214baac232b7056a506252b7574ea7738d1f"

BBCLASSEXTEND = "native nativesdk"
