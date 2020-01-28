package se.lu.humlab.langtrackapp.data

/*
* Stephan Björck
* Humanistlaboratoriet
* Lunds Universitet
* stephan.bjorck@humlab.lu.se
* */

import android.content.Context

class RepositoryFactory {
    companion object {

        private var sRepositoryQr: Repository? = null

        @JvmStatic
        @Synchronized
        fun getRepository(context: Context): Repository {
            if (sRepositoryQr == null) {
                sRepositoryQr =
                    Repository(context)
            }
            return sRepositoryQr as Repository
        }

    }
}