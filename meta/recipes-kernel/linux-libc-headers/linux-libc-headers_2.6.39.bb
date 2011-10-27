require linux-libc-headers.inc

PR = "r3"

SRC_URI += " file://connector-msg-size-fix.patch"
SRC_URI[md5sum] = "1aab7a741abe08d42e8eccf20de61e05"
SRC_URI[sha256sum] = "584d17f2a3ee18a9501d7ff36907639e538cfdba4529978b8550c461d45c61f6"
