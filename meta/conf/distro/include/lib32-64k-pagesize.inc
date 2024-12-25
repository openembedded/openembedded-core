MAX_PAGE_SIZE_FLAGS ?= " -Wl,-z,max-page-size=0x10000"
MAX_PAGE_SIZE_FLAGS:pn-lib32-systemd-boot = ""
CFLAGS:append:virtclass-multilib-lib32 = "${MAX_PAGE_SIZE_FLAGS}"
LDFLAGS:append:virtclass-multilib-lib32 = "${MAX_PAGE_SIZE_FLAGS}"
