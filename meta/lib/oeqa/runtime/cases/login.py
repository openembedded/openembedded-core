#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import subprocess
from oeqa.runtime.case import OERuntimeTestCase
import tempfile
from oeqa.runtime.decorator.package import OEHasPackage

### Status of qemu images.
#   - runqemu qemuppc64 comes up blank. (skip)
#   - qemuarmv5 comes up with multiple heads but sending "head" to screendump.
#     seems to create a png with a bad header? (skip for now, but come back to fix)
#   - qemuriscv32 and qemuloongarch64 doesn't work with testimage apparently? (skip)
#   - qemumips64 is missing mouse icon.
#   - qemumips takes forever to render and is missing mouse icon.
#   - qemuarm and qemuppc are odd as they don't resize so we need to just set width.
#   - All images have home and screen flipper icons not always rendered fully at first.
#     the sleep seems to help this out some, depending on machine load.
###

class LoginTest(OERuntimeTestCase):
    def test_screenshot(self):
        if self.td.get('MACHINE') in ("qemuppc64", "qemuarmv5", "qemuriscv32", "qemuloongarch64"):
            self.skipTest("{0} is not currently supported.".format(self.td.get('MACHINE')))

        # Set DEBUG_CREATE_IMAGES to 1 in order to populate the image-test images directory.
        DEBUG_CREATE_IMAGES="0"
        # Store failed images so we can debug them.
        failed_image_dir=self.td.get('TOPDIR') + "/failed-images/"

        ###
        # This is a really horrible way of doing this but I've not found the
        # right event to determine "The system is loaded and screen is rendered"
        #
        # Using dbus-wait for matchbox is the wrong answer because while it
        # ensures the system is up, it doesn't mean the screen is rendered.
        #
        # Checking the qmp socket doesn't work afaik either.
        #
        # One way to do this is to do compares of known good screendumps until
        # we either get expected or close to expected or we time out. Part of the
        # issue here with that is that there is a very fine difference in the
        # diff between a screendump where the icons haven't loaded yet and
        # one where they won't load. I'll look at that next, but, for now, this.
        #
        # Which is ugly and I hate it but it 'works' for various definitions of
        # 'works'.
        ###

        import time
        
        # qemumips takes forever to render. We could probably get away with 20
        # here were it not for that.
        time.sleep(40)

        with tempfile.NamedTemporaryFile(prefix="oeqa-screenshot-login", suffix=".png") as t:
            ret = self.target.runner.run_monitor("screendump", args={"filename": t.name, "format":"png"})

            # Find out size of image so we can determine where to blank out clock.
            # qemuarm and qemuppc are odd as it doesn't resize the window and returns
            # incorrect widths
            if self.td.get('MACHINE')=="qemuarm" or self.td.get('MACHINE')=="qemuppc":
                width="640"
            else:
                cmd = "identify.im7 -ping -format '%w' {0}".format(t.name)
                width = subprocess.check_output(cmd, shell=True).decode()

            rblank=int(float(width))
            lblank=rblank-40

            # Use the meta-oe version of convert, along with it's suffix. This blanks out the clock.
            cmd = "convert.im7 {0} -fill white -draw 'rectangle {1},10 {2},22' {3}".format(t.name, str(rblank), str(lblank), t.name)
            convert_out=subprocess.check_output(cmd, shell=True).decode()

            if DEBUG_CREATE_IMAGES=="1":
                # You probably aren't interested in this as it's just to create the images we compare against.
                import shutil
                shutil.copy2(t.name, "{0}/meta/files/image-tests/core-image-sato-{1}.png".format(self.td.get('COREBASE'), \
                                                                                                 self.td.get('MACHINE')))
                self.skipTest("Created a reference image for {0} and placed it in {1}/meta/files/image-tests/.".format(self.td.get('MACHINE'), self.td.get('COREBASE')))
            else:
                # Use the meta-oe version of compare, along with it's suffix.
                cmd = "compare.im7 -metric MSE {0} {1}/meta/files/image-tests/core-image-sato-{2}.png /dev/null".format(t.name, \
                                                                                                                        self.td.get('COREBASE'), \
                                                                                                                        self.td.get('MACHINE'))
                compare_out = subprocess.run(cmd, shell=True, capture_output=True, text=True)
                diff=float(compare_out.stderr.replace("(", "").replace(")","").split()[1])
                if diff > 0:
                    from datetime import datetime
                    import shutil
                    import os
                    try:
                        os.mkdir(failed_image_dir)
                    except FileExistsError:
                        # directory exists
                        pass
                    # Keep a copy of the failed screenshot so we can see what happened.
                    failedfile="{0}/failed-{1}-core-image-sato-{2}.png".format(failed_image_dir, \
                                                                               datetime.timestamp(datetime.now()), \
                                                                               self.td.get('MACHINE'))
                    shutil.copy2(t.name, failedfile)
                    self.fail("Screenshot diff is {0}. Failed image stored in {1}".format(str(diff), failedfile))
                else:
                    self.assertEqual(0, diff, "Screenshot diff is {0}.".format(str(diff)))
