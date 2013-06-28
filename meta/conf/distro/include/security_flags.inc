SECURITY_CFLAGS ?= "-fstack-protector-all -pie -fpie -D_FORTIFY_SOURCE=2"
SECURITY_LDFLAGS ?= "-Wl,-z,relro,-z,now"

# Curl seems to check for FORTIFY_SOURCE in CFLAGS, but even assigned
# to CPPFLAGS it gets picked into CFLAGS in bitbake.
#TARGET_CPPFLAGS_pn-curl += "-D_FORTIFY_SOURCE=2"
SECURITY_CFLAGS_pn-curl = "-fstack-protector-all -pie -fpie"
SECURITY_CFLAGS_pn-ppp = "-fstack-protector-all -D_FORTIFY_SOURCE=2"
SECURITY_CFLAGS_pn-eglibc = ""
SECURITY_CFLAGS_pn-eglibc-initial = ""
SECURITY_CFLAGS_pn-zlib = "-fstack-protector-all -D_FORTIFY_SOURCE=2"
SECURITY_CFLAGS_pn-gcc-runtime = "-fstack-protector-all -D_FORTIFY_SOURCE=2"
SECURITY_CFLAGS_pn-libgcc = "-fstack-protector-all -D_FORTIFY_SOURCE=2"
SECURITY_CFLAGS_pn-tcl = "-fstack-protector-all -D_FORTIFY_SOURCE=2"
SECURITY_CFLAGS_pn-libcap = "-fstack-protector-all -D_FORTIFY_SOURCE=2"
SECURITY_CFLAGS_pn-python-smartpm = "-fstack-protector-all -D_FORTIFY_SOURCE=2"
SECURITY_CFLAGS_pn-python-imaging = "-fstack-protector-all -D_FORTIFY_SOURCE=2"
SECURITY_CFLAGS_pn-python-pycurl = "-fstack-protector-all -D_FORTIFY_SOURCE=2"
SECURITY_CFLAGS_pn-kexec-tools = "-fstack-protector-all -D_FORTIFY_SOURCE=2"

# These 2 have text relco errors with the pie options enabled
SECURITY_CFLAGS_pn-pulseaudio = "-fstack-protector-all -D_FORTIFY_SOURCE=2"
SECURITY_CFLAGS_pn-ltp = "-fstack-protector-all -D_FORTIFY_SOURCE=2"

TARGET_CFLAGS_append = " ${SECURITY_CFLAGS}"
TARGET_LDFLAGS_append = " ${SECURITY_LDFLAGS}"
