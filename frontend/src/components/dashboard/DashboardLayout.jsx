import React from 'react'
import { Outlet,Navigate } from 'react-router-dom'
import ManageUser from './ManageUser'

const DashboardLayout = () => {
  const user = localStorage.getItem("user");
  const userObj = JSON.parse(user);

  return (
    <div className='flex gap-4 flex-col md:flex-row'>
      {user ? (
        <>
          {userObj.role === "admin" ? (
            <>
              <ManageUser />
              <Outlet />
            </>
          ) : (
            <Navigate to='/' />
          )}
        </>
      ) : (
        <Navigate to='/login' />
      )}
    </div>
  )
}

export default DashboardLayout