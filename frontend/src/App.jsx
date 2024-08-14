import './App.css'
import { Outlet } from 'react-router-dom'
import Navbar from './components/navbar/Navbar'
// import MyFooter from './components/MyFooter'

function App() {

  return (
    <>
      <Navbar/>
      
      <div className='min-h-[77vh]'>
        <Outlet />
      </div>

      {/* <MyFooter/> */}
    </>
  )
}

export default App
