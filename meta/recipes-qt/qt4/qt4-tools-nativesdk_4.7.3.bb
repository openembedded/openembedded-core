require qt4-tools-nativesdk.inc

PR = "${INC_PR}.0"

SRC_URI += "file://blacklist-diginotar-certs.diff"

SRC_URI[md5sum] = "49b96eefb1224cc529af6fe5608654fe"
SRC_URI[sha256sum] = "d02b6fd69d089c01f4a787aa18175d074ccaecf8980a5956e328c2991905937e"
