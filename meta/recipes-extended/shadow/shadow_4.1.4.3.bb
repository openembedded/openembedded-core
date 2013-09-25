require shadow.inc

# Build falsely assumes that if --enable-libpam is set, we don't need to link against
# libcrypt. This breaks chsh.
BUILD_LDFLAGS_append_class-target = " ${@base_contains('DISTRO_FEATURES', 'pam', base_contains('DISTRO_FEATURES', 'libc-crypt',  '-lcrypt', '', d), '', d)}"

BBCLASSEXTEND = "native nativesdk"



