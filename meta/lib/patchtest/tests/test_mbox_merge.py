# Check if mbox was merged by patchtest
#
# Copyright (C) 2016 Intel Corporation
#
# SPDX-License-Identifier: GPL-2.0-only

import subprocess
import base
from data import PatchTestInput

def headlog():
    output = subprocess.check_output(
        "cd %s; git log --pretty='%%h#%%aN#%%cD:#%%s' -1" % PatchTestInput.repodir,
        universal_newlines=True,
        shell=True
        )
    return output.split('#')

class Merge(base.Base):
    def test_series_merge_on_head(self):
        self.skip("Merge test is disabled for now")
        if PatchTestInput.repo.branch != "master":
            self.skip("Skipping merge test since patch is not intended for master branch. Target detected is %s" % PatchTestInput.repo.branch)
        if not PatchTestInput.repo.ismerged:
            commithash, author, date, shortlog = headlog()
            self.fail('Series does not apply on top of target branch. Rebase your series and ensure the target is correct',
                      data=[('Targeted branch', '%s (currently at %s)' % (PatchTestInput.repo.branch, commithash))])
