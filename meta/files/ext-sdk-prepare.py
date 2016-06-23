#!/usr/bin/env python

# Prepare the build system within the extensible SDK

import sys
import os
import subprocess

def exec_watch(cmd, **options):
    """Run program with stdout shown on sys.stdout"""
    if isinstance(cmd, str) and not "shell" in options:
        options["shell"] = True

    process = subprocess.Popen(
        cmd, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, **options
    )

    buf = ''
    while True:
        out = process.stdout.read(1)
        if out:
            sys.stdout.write(out)
            sys.stdout.flush()
            buf += out
        elif out == '' and process.poll() != None:
            break

    return process.returncode, buf

def main():
    if len(sys.argv) < 2:
        sdk_targets = []
    else:
        sdk_targets = ' '.join(sys.argv[1:]).split()
    if not sdk_targets:
        # Just do a parse so the cache is primed
        ret, _ = exec_watch('bitbake -p')
        return ret

    print('Preparing SDK for %s...' % ', '.join(sdk_targets))

    ret, out = exec_watch('BB_SETSCENE_ENFORCE=1 bitbake %s' % ' '.join(sdk_targets))
    if ret:
        return ret

if __name__ == "__main__":
    try:
        ret = main()
    except Exception:
        ret = 1
        import traceback
        traceback.print_exc()
    sys.exit(ret)
