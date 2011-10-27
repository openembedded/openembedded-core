require linux-libc-headers.inc

PR = "r3"

SRC_URI += " file://connector-msg-size-fix.patch"
SRC_URI[md5sum] = "89f681bc7c917a84aa7470da6eed5101"
SRC_URI[sha256sum] = "2920c4cd3e87fe40ebee96d28779091548867e1c36f71c1fc3d07e6d5802161f"
